package com.freelek.apps.freelekdz.push_notification_firebase;

import android.util.Log;

import com.freelek.apps.freelekdz.appconfig.AppConfig;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class FirebaseMessagingServiceIns extends FirebaseMessagingService {

    public static final String TAG = "FirebaseMessaging";


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (AppConfig.APP_DEBUG) {
            Log.d(TAG, "From " + remoteMessage.toString());
            Log.d(TAG, "From: " + remoteMessage.getFrom());
        }

        Map<String, String> messageFromOwnServer = remoteMessage.getData();
        try {
            showNotification(messageFromOwnServer);
        } catch (Exception e) {
            try {
                //showNotification(remoteMessage.getNotification().getBody());
            } catch (Exception e1) {
                if (AppConfig.APP_DEBUG)
                    e1.printStackTrace();
            }

            if (AppConfig.APP_DEBUG)
                e.printStackTrace();
        }


    }


    private void showNotification(Map<String, String> message) {

        if (AppConfig.APP_DEBUG) {
            Log.e(TAG, "InCommingData " + message.toString());
        }

        DTNotificationManager mCampaignNotifManager = new DTNotificationManager(this, message);
        mCampaignNotifManager.push();

    }
}
