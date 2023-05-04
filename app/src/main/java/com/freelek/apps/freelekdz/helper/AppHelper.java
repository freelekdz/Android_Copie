package com.freelek.apps.freelekdz.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.freelek.apps.freelekdz.appconfig.Constances;
import com.freelek.apps.freelekdz.classes.User;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AppHelper {

    private static ProgressDialog mDialog;

    public static List<User> prepareListWithHeaders(List<User> list) {

        for (int i = 0; i < list.size(); i++) {

            User user = list.get(i);
            if (i == 0) {
                user.setWithHeader(true);
            } else if (i > 0) {

                try {

                    if (list.get(0).getDistance() <= Constances.DISTANCE_CONST &&
                            (user.getDistance() > Constances.DISTANCE_CONST)) {
                        user.setWithHeader(true);
                        list.set(i, user);
                        break;

                    } else if (list.get(0).getDistance() > Constances.DISTANCE_CONST) {
                        break;
                    }

                } catch (Exception e) {

                }

            }

        }

        return list;
    }


    /**
     * method to check if android version is lollipop
     *
     * @return this return value
     */
    public static boolean isAndroid5() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }


    /**
     * method to load json files from asset directory
     *
     * @param mContext this is  parameter for loadCountriesJSONFromAsset  method
     * @return return value
     */
    public static String loadCountriesJSONFromAsset(Context mContext) {
        String json = null;
        try {
            InputStream is = mContext.getAssets().open("data/country_phones.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * Check permissions methods for Android M
     */



    public static String extractYoutubeVideoId(String ytUrl) {

        String vId = null;

        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(ytUrl);

        if(matcher.find()){
            vId= matcher.group();
        }
        return vId;
    }


    public static Drawable loadDrawable(Context mContext, String path) {
        // load image
        try {
            // get input stream
            InputStream ims = mContext.getAssets().open(path);
            // load image as Drawable
            // set image to ImageView
            return Drawable.createFromStream(ims, null);
        } catch (IOException ex) {
            return null;
        }
    }



}
