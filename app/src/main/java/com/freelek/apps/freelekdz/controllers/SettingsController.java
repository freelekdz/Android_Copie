package com.freelek.apps.freelekdz.controllers;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.freelek.apps.freelekdz.AppController;
import com.freelek.apps.freelekdz.R;
import com.freelek.apps.freelekdz.classes.Module;
import com.freelek.apps.freelekdz.classes.Setting;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Droideve on 12/14/2017.
 */

public class SettingsController {

    public static boolean updateModules(final RealmList<Module> list) {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Module mModule : list) {
                    realm.copyToRealmOrUpdate(mModule);
                }
            }
        });
        return true;
    }

    public static boolean updateSettings(final RealmList<Setting> list) {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Setting setting : list) {
                    realm.copyToRealmOrUpdate(setting);
                }
            }
        });
        return true;
    }


    public static Setting findSettingFiled(final String key) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Setting.class).equalTo("_key", key).findFirst();
    }


    public static boolean isModuleEnabled(final String module_name) {
        Realm realm = Realm.getDefaultInstance();
        Module mModule = realm.where(Module.class).equalTo("name", module_name).findFirst();
        return mModule != null && mModule.isEnabled() == 1;
    }



    public static void requestPermissionM(FragmentActivity context, String[] perms) {

        for (String permission : perms) {// Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(context,
                    permission)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                        permission)) {



                    Toast.makeText(context, R.string.permission_disabled_notice, Toast.LENGTH_LONG).show();
                } else {

                    // No explanation needed, we can request the permission.
                    try {
                        ActivityCompat.requestPermissions(context,
                                new String[]{permission}, 101);
                    } catch (Exception e) {
                        Log.e("Permission", e.getMessage());
                    }

                }
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            }

        }
    }

    public static boolean rateOnApp(FragmentActivity activity) {

        final SharedPreferences sharedPref = AppController.getInstance()
                .getSharedPreferences("StoreController", Context.MODE_PRIVATE);
        boolean i = sharedPref.getBoolean("rated", false);
        int nbr = sharedPref.getInt("nbr", 0);

        if (nbr > 4) {
            return true;
        }


        if (i == false) {
            showRate(activity);
            return false;
        }

        return true;
    }

    private static void showRate(final FragmentActivity activity) {

        final SharedPreferences sharedPref = AppController.getInstance()
                .getSharedPreferences("StoreController", Context.MODE_PRIVATE);

        new android.app.AlertDialog.Builder(activity)
                .setTitle("Exit!")
                .setMessage(activity.getString(R.string.rateUs))
                .setPositiveButton(activity.getString(R.string.rateIt), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            activity.startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
                        }


                        int nbr = sharedPref.getInt("nbr", 0);
                        nbr++;
                        sharedPref.edit().putInt("nbr", nbr).commit();
                        sharedPref.edit().putBoolean("rated", true).commit();


                    }
                })
                .setNegativeButton(activity.getString(R.string.options_exit), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        int nbr = sharedPref.getInt("nbt", 0);
                        nbr++;
                        sharedPref.edit().putInt("nbr", nbr).commit();

                        activity.moveTaskToBack(true);
                        ActivityCompat.finishAffinity(activity);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }

}
