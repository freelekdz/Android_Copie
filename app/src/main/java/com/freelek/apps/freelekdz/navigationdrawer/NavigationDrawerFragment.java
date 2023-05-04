package com.freelek.apps.freelekdz.navigationdrawer;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.freelek.apps.freelekdz.R;
import com.freelek.apps.freelekdz.Services.BusStation;
import com.freelek.apps.freelekdz.activities.AboutActivity;
import com.freelek.apps.freelekdz.activities.CustomSearchActivity;
import com.freelek.apps.freelekdz.activities.MapStoresListActivity;
import com.freelek.apps.freelekdz.activities.SettingActivity;
import com.freelek.apps.freelekdz.activities.SplashActivity;
import com.freelek.apps.freelekdz.adapter.navigation.SimpleListAdapterNavDrawer;
import com.freelek.apps.freelekdz.appconfig.AppConfig;
import com.freelek.apps.freelekdz.booking.views.activities.BookingListActivity;
import com.freelek.apps.freelekdz.classes.HeaderItem;
import com.freelek.apps.freelekdz.classes.Item;
import com.freelek.apps.freelekdz.controllers.sessions.SessionsController;
import com.freelek.apps.freelekdz.customView.AdvancedWebViewActivity;
import com.freelek.apps.freelekdz.fragments.V2MainFragment;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class NavigationDrawerFragment extends Fragment implements SimpleListAdapterNavDrawer.ClickListener {

    public static final String PREF_FILE_NAME = "testpref";
    public static final String KEY_USER_LEARNED_DRAWER = "learned_user_drawer";
    public static int INT_CHAT_BOX = 5;
    private static DrawerLayout mDrawerLayout;
    List<Item> listItems = Arrays.asList();
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private boolean mUserLearedLayout;
    private boolean mFromSaveInstanceState;
    private View containerView;
    private RecyclerView drawerList;


    //init request http
    private SimpleListAdapterNavDrawer adapter;

    public static DrawerLayout getInstance() {
        return mDrawerLayout;
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(preferenceName, preferenceValue);
        edit.apply();

    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mUserLearedLayout = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mFromSaveInstanceState = true;
        }


    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.navigation_drawer_content, container, false);

        rootView.setClickable(true);

        drawerList = rootView.findViewById(R.id.drawerLayout);
        drawerList.setVisibility(View.VISIBLE);

        adapter = new SimpleListAdapterNavDrawer(getActivity(), getData());

        drawerList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        drawerList.setLayoutManager(mLayoutManager);
        drawerList.setAdapter(adapter);

        adapter.setClickListener(this);

        return rootView;

    }

    public List<Item> getData() {

        listItems = new ArrayList<Item>();


        HeaderItem header_item = new HeaderItem();
        header_item.setName(getResources().getString(R.string.Home));
        header_item.setEnabled(true);


        Item homeItem = new Item();
        homeItem.setName(getResources().getString(R.string.Home));
        homeItem.setIconDraw(CommunityMaterial.Icon2.cmd_home_outline);
        homeItem.setID(Menu.HOME_ID);

        Item webdashboard = new Item();
        webdashboard.setName(getResources().getString(R.string.ManageThings));
        webdashboard.setIconDraw(CommunityMaterial.Icon.cmd_briefcase_outline);
        webdashboard.setID(Menu.MANAGE_YOUR_BUSINESS);

        Item mapStoresItem = new Item();
        mapStoresItem.setName(getResources().getString(R.string.MapStoresMenu));
        mapStoresItem.setIconDraw(CommunityMaterial.Icon2.cmd_map_outline);
        mapStoresItem.setID(Menu.MAP_STORES);

        Item bookingItem = new Item();
        bookingItem.setName(getResources().getString(R.string.booking));
        bookingItem.setIconDraw(CommunityMaterial.Icon.cmd_calendar);
        bookingItem.setID(Menu.BOOKING);

        Item aboutItem = new Item();
        aboutItem.setName(getResources().getString(R.string.about));
        aboutItem.setIconDraw(CommunityMaterial.Icon2.cmd_information_outline);
        aboutItem.setID(Menu.ABOUT);


        Item settingItem = new Item();
        settingItem.setName(getResources().getString(R.string.Settings));
        settingItem.setIconDraw(CommunityMaterial.Icon2.cmd_settings_outline);
        settingItem.setID(Menu.SETTING);


        if (header_item.isEnabled())
            listItems.add(header_item);

        //HOME
        if (homeItem.isEnabled())
            listItems.add(homeItem);


        //GEO Stores
        listItems.add(mapStoresItem);

        //Booking
        if (bookingItem.isEnabled())
            listItems.add(bookingItem);


        //Settings
        if (AppConfig.ENABLE_WEB_DASHBOARD) {
            listItems.add(webdashboard);
        }

        if (SessionsController.isLogged()) {

            Item logout = new Item();
            logout.setName(getResources().getString(R.string.Logout));
            logout.setIconDraw(CommunityMaterial.Icon.cmd_exit_to_app);
            logout.setID(Menu.LOGOUT);

            listItems.add(logout);

        }


        //About US
        if (aboutItem.isEnabled())
            listItems.add(aboutItem);

        //Settings
        if (settingItem.isEnabled())
            listItems.add(settingItem);


        return listItems;
    }

    public void setUp(int FragId, DrawerLayout drawerlayout, final Toolbar toolbar) {

        containerView = getView().findViewById(FragId);
        mDrawerLayout = drawerlayout;

        //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                drawerlayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
                if (!mUserLearedLayout) {
                    mUserLearedLayout = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearedLayout + "");
                }


                getActivity().invalidateOptionsMenu();

            }

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();

            }


            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

            }
        };

        if (!mUserLearedLayout && !mFromSaveInstanceState) {
            mDrawerLayout.closeDrawer(containerView);
        }


        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INT_CHAT_BOX) {

            adapter.getData().get(1).setNotify(0);
            adapter.update(1, adapter.getData().get(1));

        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void itemClicked(View view, int position) {


        Item item = adapter.getData().get(position);
        if (item instanceof Item) {
            switch (item.getID()) {
                case Menu.HOME_ID:

                    if (mDrawerLayout != null)
                        mDrawerLayout.closeDrawers();
                    V2MainFragment mf = (V2MainFragment) getFragmentManager().findFragmentByTag(V2MainFragment.TAG);
                    mf.setCurrentFragment(0);

                    break;
                case Menu.MANAGE_YOUR_BUSINESS:

                    if (!AppConfig.ENABLE_WEB_DASHBOARD)
                        break;

                    if (mDrawerLayout != null)
                        mDrawerLayout.closeDrawers();

                    String url = AppConfig.BASE_URL + "/dashboard";
                    if (SessionsController.isLogged() && SessionsController.getSession() != null && SessionsController.getSession().getUser() != null) {
                        url = url + "&session=" +
                                SessionsController.getSession().getUser().getUsername();
                    }


                    Intent intent = new Intent(getActivity(), AdvancedWebViewActivity.class);
                    intent.putExtra("BMLink", url);
                    startActivity(intent);

                    if (AppConfig.APP_DEBUG)
                        Log.e("BMLink", url);


                    break;
                case Menu.BOOKING:

                    if (SessionsController.isLogged()) {
                        Intent bookingIntent = new Intent(getActivity(), BookingListActivity.class);
                        startActivity(bookingIntent);
                        getActivity().overridePendingTransition(R.anim.lefttoright_enter, R.anim.lefttoright_exit);
                    } else {
                        startActivity(new Intent(getActivity(), CustomSearchActivity.LoginActivityV2.class));
                        getActivity().overridePendingTransition(R.anim.lefttoright_enter, R.anim.lefttoright_exit);
                        getActivity().finish();
                    }


                    break;
                case Menu.ABOUT:

                    if (mDrawerLayout != null)
                        mDrawerLayout.closeDrawers();

                    startActivity(new Intent(getActivity(), AboutActivity.class));
                    getActivity().overridePendingTransition(R.anim.lefttoright_enter, R.anim.lefttoright_exit);

                    break;

                case Menu.SETTING:

                    if (mDrawerLayout != null)
                        mDrawerLayout.closeDrawers();

                    startActivity(new Intent(getActivity(), SettingActivity.class));
                    getActivity().overridePendingTransition(R.anim.lefttoright_enter, R.anim.lefttoright_exit);

                    break;
                case Menu.MAP_STORES:
                    startActivity(new Intent(getActivity(), MapStoresListActivity.class));
                    getActivity().overridePendingTransition(R.anim.lefttoright_enter, R.anim.lefttoright_exit);

                    break;
                case Menu.LOGOUT:

                    SessionsController.logOut();
                    ActivityCompat.finishAffinity(getActivity());
                    startActivity(new Intent(getActivity(), SplashActivity.class));

                    break;


            }
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        BusStation.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusStation.getBus().unregister(this);
    }

    @Subscribe
    public void onToggle(NavigationDrawerEvent object) {
        if (object.state == 1) {

            if (mDrawerLayout.isDrawerOpen(containerView))
                mDrawerLayout.closeDrawer(containerView);
            else
                mDrawerLayout.openDrawer(containerView);

        }
    }

    private static class Menu {
        static final int HOME_ID = 1;
        static final int MANAGE_YOUR_BUSINESS = 2;
        static final int MAP_STORES = 3;
        static final int FAV = 4;
        static final int MY_EVENT = 5;
        static final int ABOUT = 6;
        static final int SETTING = 7;
        static final int BOOKING = 8;
        static final int LOGOUT = 9;
    }

    public static class NavigationDrawerEvent {
        private int state = 0;

        public NavigationDrawerEvent(int state) {
            this.state = state;
        }
    }


}
