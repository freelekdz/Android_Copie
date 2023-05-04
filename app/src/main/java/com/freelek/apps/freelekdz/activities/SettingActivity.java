package com.freelek.apps.freelekdz.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import com.freelek.apps.freelekdz.BuildConfig;
import com.freelek.apps.freelekdz.R;
import com.freelek.apps.freelekdz.appconfig.Constances;
import com.google.android.material.appbar.AppBarLayout;
import com.wuadam.awesomewebview.AwesomeWebView;

public class SettingActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {


    private static int getResIdFromAttribute(final Activity activity, final int attr) {
        if (attr == 0) {
            return 0;
        }
        final TypedValue typedvalueattr = new TypedValue();
        activity.getTheme().resolveAttribute(attr, typedvalueattr, true);
        return typedvalueattr.resourceId;
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        AppBarLayout bar;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
            bar = (AppBarLayout) LayoutInflater.from(this).inflate(R.layout.appbar_setting, root, false);
            root.addView(bar, 0);
        } else {
            ViewGroup root = findViewById(android.R.id.content);
            ListView content = (ListView) root.getChildAt(0);
            root.removeAllViews();
            bar = (AppBarLayout) LayoutInflater.from(this).inflate(R.layout.appbar_setting, root, false);
            bar.setBackground(getDrawable(R.color.white));

            int height;
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            } else {
                height = bar.getHeight();
            }

            content.setPadding(0, height, 0, 0);

            root.addView(content);
            root.addView(bar);
        }


        Toolbar Tbar = (Toolbar) bar.getChildAt(0);
        Tbar.setClickable(true);

        int resId = getResIdFromAttribute(this, R.attr.homeAsUpIndicator);
        Drawable arrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_close_white_24dp, null);
        // arrow.setColorFilter(ResourcesCompat.getColor(getResources(),R.color.white,null), PorterDuff.Mode.MULTIPLY);
        Tbar.setNavigationIcon(arrow);

        TextView title = Tbar.findViewById(R.id.toolbar_title);
        TextView toolbar_description = Tbar.findViewById(R.id.toolbar_subtitle);
        title.setTextColor(R.color.white);
        toolbar_description.setTextColor(R.color.white);

        toolbar_description.setVisibility(View.GONE);
        title.setText(getString(R.string.settings));


        Tbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        setupSimplePreferencesScreen();

    }

    @SuppressWarnings("deprecation")

    //private SeekBarPreference _seekBarPref;
    private void setupSimplePreferencesScreen() {
        addPreferencesFromResource(R.xml.settings);

        Preference app_version = findPreference("app_version");
        app_version.setSummary(BuildConfig.VERSION_NAME);


        Preference app_term_of_uses = findPreference("app_term_of_uses");
        Preference app_privacy = findPreference("app_privacy");

         ListPreference distance_unit = (ListPreference) findPreference("distance_unit");

        distance_unit.setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {

                        //todo : set distance


                        //It is required to recreate the activity to reflect the change in UI.
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                            finishAffinity();
                        else
                            ActivityCompat.finishAffinity(getParent());

                        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                        startActivity(intent);

                        return true;
                    }
                }

        );



        app_term_of_uses.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {


                new AwesomeWebView.Builder(SettingActivity.this)
                        .showMenuOpenWith(false)
                        .statusBarColorRes(R.color.colorPrimary)
                        .theme(R.style.FinestWebViewAppTheme)
                        .titleColor(
                                ResourcesCompat.getColor(getResources(), R.color.white, null)
                        ).urlColor(
                        ResourcesCompat.getColor(getResources(), R.color.white, null)
                ).show(Constances.TERMS_OF_USE_URL);


                return false;
            }
        });

        app_privacy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {


                new AwesomeWebView.Builder(SettingActivity.this)
                        .showMenuOpenWith(false)
                        .statusBarColorRes(R.color.colorPrimary)
                        .theme(R.style.FinestWebViewAppTheme)
                        .titleColor(
                                ResourcesCompat.getColor(getResources(), R.color.white, null)
                        ).urlColor(
                        ResourcesCompat.getColor(getResources(), R.color.white, null)
                ).show(Constances.PRIVACY_POLICY_URL);

                return false;
            }
        });


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        int radius = PreferenceManager.getDefaultSharedPreferences(this).getInt("distance_value", 100);


        String val = String.valueOf(radius);
        if (radius == 100) {
            val = "+" + radius;
        }
//        _seekBarPref.setSummary(
//                String.format(getString(R.string.settings_notification_distance_msg),val)
//        );

    }


}
