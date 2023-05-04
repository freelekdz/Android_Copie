package com.freelek.apps.freelekdz.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.freelek.apps.freelekdz.R;
import com.freelek.apps.freelekdz.appconfig.AppConfig;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;

public class Utils {


    public static final String DEFAULT_VALUE = "N/A";
    private static String SP_NAME = "q2sUn5aZDmL56";
    private static String SP_NAME_KEY = "q2sUn5aZDmL56tOoKeN";

    public static Bitmap flip(Bitmap src) {

        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public static int dp_get_id_from_url(String url, String prefix) {

        if (!Utils.isValidURL(url))
            return 0;

        String[] list = url.split("/");

        try {

            for (int i = 0; i < list.length; i++) {

                if (prefix.equals(list[i])) {
                    String uri = list[i + 1];

                    if (uri.equals("id")) {
                        String id = list[i + 2];
                        if (AppConfig.APP_DEBUG)
                            Log.e("dp_get_id_from_url", prefix + " " + Integer.parseInt(id) + " closed");
                        return Integer.parseInt(id);
                    }

                }
            }

        } catch (Exception e) {
            if (AppConfig.APP_DEBUG)
                e.printStackTrace();
        }


        return 0;
    }

    public static boolean isValidURL(String url) {

        URL u = null;

        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            return false;
        }

        try {
            u.toURI();
        } catch (URISyntaxException e) {
            return false;
        }

        return true;
    }

    public static Drawable changeDrawableIconMap(Context context, int resId) {

        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), resId, null);
        PorterDuff.Mode mode = PorterDuff.Mode.SRC_ATOP;
        drawable.setColorFilter(ResourcesCompat.getColor(context.getResources(), R.color.colorPrimary, null), mode);

        return drawable;
    }



    public static String getToken(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SP_NAME_KEY, "");
    }

    public static String getDistanceByKm(double meters) {

        String FINAL_VALUE = "M";
        if (meters > 0) {

            if (meters > 1000) {

                FINAL_VALUE = "Km";

            }
        } else {
            FINAL_VALUE = "";
        }

        return FINAL_VALUE;

    }

    public static String getDistanceMiles(double meters) {

        //convert meter to feet
        double feet = meters * 3.28084;

        String FINAL_VALUE = "feet";
        if (feet > 0) {

            if (feet >= 5280) {

                FINAL_VALUE = "miles";

            }
        } else {
            FINAL_VALUE = "";
        }

        return FINAL_VALUE;

    }


    public static Boolean isNearMAXDistanceKM(double meters) {

        return meters >= 0 && meters <= 100000;

    }


    public static Boolean isNearMAXDistanceMiles(double meters) {

        //convert meter to feet
        double feet = meters * 3.28084;

        //100km
        return feet >= 0 && feet <= 328083.888;

    }


    public static String prepareDistanceKm(double meters) {

        String FINAL_VALUE = DEFAULT_VALUE + " ";

        if (meters >= 1000 && meters <= 100000) {

            double kilometers = 0.0;
            kilometers = meters * 0.001;

            DecimalFormat decim = new DecimalFormat("#.##");
            FINAL_VALUE = decim.format(kilometers) + "";

        } else if (meters > 100000) {

            FINAL_VALUE = "+100";

        } else if (meters < 1000) {

            FINAL_VALUE = ((int) meters) + "";

        }


        return FINAL_VALUE;
    }


    public static String prepareDistanceMiles(double meters) {

        //convert meter to feet
        double feet = meters * 3.28084;

        String FINAL_VALUE = DEFAULT_VALUE + " ";

        if (feet >= 5280 && feet <= 328083.888) {

            double miles = feet * 0.000189394;

            DecimalFormat decim = new DecimalFormat("#.##");
            FINAL_VALUE = decim.format(miles) + "";

        } else if (feet > 328083.888) {

            FINAL_VALUE = "+100";

        } else if (feet < 5280) {

            FINAL_VALUE = ((int) feet) + "";

        }

        return FINAL_VALUE;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static boolean toggleArrow(boolean show, View view) {
        return toggleArrow(show, view, true);
    }


    public static boolean toggleArrow(boolean show, View view, boolean delay) {
        if (show) {
            view.animate().setDuration(delay ? 200 : 0).rotation(180);
            return true;
        } else {
            view.animate().setDuration(delay ? 200 : 0).rotation(0);
            return false;
        }
    }

    public static int dip2pix(@NonNull Context context, int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
                context.getResources().getDisplayMetrics());
    }

    public static void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawablesRelative()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    public static class Params {

        private Bundle params;


        @Override
        public String toString() {

            return params.toString();
        }
    }

    public static String capitalizeString(String str) {
        String retStr = str;
        try { // We can face index out of bound exception if the string is null
            retStr = str.substring(0, 1).toUpperCase() + str.substring(1);
        }catch (Exception e){}
        return retStr;
    }

}

