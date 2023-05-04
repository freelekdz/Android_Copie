package com.freelek.apps.freelekdz.push_notification_firebase;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.freelek.apps.freelekdz.AppController;
import com.freelek.apps.freelekdz.GPS.GPStracker;
import com.freelek.apps.freelekdz.appconfig.Constances;
import com.freelek.apps.freelekdz.classes.Guest;
import com.freelek.apps.freelekdz.controllers.sessions.GuestController;
import com.freelek.apps.freelekdz.dtmessenger.TokenInstance;
import com.freelek.apps.freelekdz.network.ServiceHandler;
import com.freelek.apps.freelekdz.network.VolleySingleton;
import com.freelek.apps.freelekdz.network.api_request.SimpleRequest;
import com.freelek.apps.freelekdz.parser.api_parser.GuestParser;
import com.freelek.apps.freelekdz.parser.tags.Tags;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.freelek.apps.freelekdz.appconfig.AppConfig.APP_DEBUG;

import androidx.annotation.NonNull;


public class FirebaseInstanceIDService extends FirebaseMessagingService {


    private static final String TAG = "FirebaseInstanceID";

    public static void load() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            updateServer("");
                            Log.w(this.getClass().getName(), "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        updateServer(token);
                    }
                });

    }

    public static void updateServer(String token){

        RequestQueue queue = VolleySingleton.getInstance(AppController.getInstance()).getRequestQueue();
        SimpleRequest request = new SimpleRequest(Request.Method.POST,
                Constances.API.API_USER_REGISTER_TOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    if (APP_DEBUG)
                        Log.e("registerTokenResponse", response);

                    JSONObject js = new JSONObject(response);

                    GuestParser mGuestParser = new GuestParser(js);
                    int success = Integer.parseInt(mGuestParser.getStringAttr(Tags.SUCCESS));
                    if (success == 1) {
                        final List<Guest> list = mGuestParser.getData();
                        if (list.size() > 0) {
                            GuestController.saveGuest(list.get(0));
                            refreshPositionGuest(list.get(0), AppController.getInstance());
                        }
                    }

                } catch (JSONException e) {
                    if (APP_DEBUG)
                        e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (APP_DEBUG) {
                    Log.e("ERROR_Firebase", error.toString());
                }

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("fcm_id", token);
                params.put("sender_id", TokenInstance.getSenderID());
                params.put("platform", "android");
                params.put("mac_adr", ServiceHandler.getMacAddr());

                if (APP_DEBUG) {
                    Log.e(TAG, "TokenToSend" + token);
                    Log.e("reloadToken", params.toString());
                }

                return params;
            }

        };


        request.setRetryPolicy(new DefaultRetryPolicy(SimpleRequest.TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);

    }

    private static void refreshPositionGuest(final Guest mGuest, final Context context) {


        GPStracker gps = new GPStracker(context);
        if (mGuest != null && gps.canGetLocation()) {

            final int user_id = mGuest.getId();
            final double lat = gps.getLatitude();
            final double lng = gps.getLongitude();

            SimpleRequest request = new SimpleRequest(Request.Method.POST,
                    Constances.API.API_REFRESH_POSITION, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {

                    if (APP_DEBUG)
                        Log.e("response", response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (APP_DEBUG)
                        Log.e("ERROR", error.toString());

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("guest_id", String.valueOf(user_id));
                    params.put("lat", String.valueOf(lat));
                    params.put("lng", String.valueOf(lng));
                    params.put("platform", "android");

                    if (APP_DEBUG)
                        Log.e("onRefreshSync", params.toString());

                    return params;
                }

            };


            request.setRetryPolicy(new DefaultRetryPolicy(SimpleRequest.TIME_OUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            VolleySingleton.getInstance(context).getRequestQueue().add(request);

        }
    }

    @Override
    public void onNewToken(String s) {
        Log.e("NEW_TOKEN", s);
    }

}