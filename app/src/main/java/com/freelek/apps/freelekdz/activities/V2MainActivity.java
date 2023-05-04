package com.freelek.apps.freelekdz.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.freelek.apps.freelekdz.R;
import com.freelek.apps.freelekdz.controllers.sessions.SessionsController;
import com.freelek.apps.freelekdz.customView.SwipeDisabledViewPager;
import com.freelek.apps.freelekdz.fragments.AuthenticationFragment;
import com.freelek.apps.freelekdz.fragments.CategoryFragment;
import com.freelek.apps.freelekdz.fragments.CustomSearchFragment;
import com.freelek.apps.freelekdz.fragments.HomeFragment;
import com.freelek.apps.freelekdz.fragments.NotificationChatVPFragment;
import com.freelek.apps.freelekdz.fragments.ProfileFragment;
import com.freelek.apps.freelekdz.utils.CommunApiCalls;
import com.freelek.apps.freelekdz.utils.Tools;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class V2MainActivity extends AppCompatActivity {


    //navigation bottom
    @BindView(R.id.navigation_bottom)
    View navigation_bottom_view;

    //viewpager
    @BindView(R.id.viewpager)
    SwipeDisabledViewPager viewPager;

    private BottomNavigationView navigation;

    private MenuItem prevMenuItem;

    private ActionBar actionBar;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v2_activity_main);
        ButterKnife.bind(this);


        //init toolbar
        //initSearchBar();
        initToolbar();

        //init view pager adapter
        initViewPagerAdapter();

        //init navigation drawer
        initNavigationMenu();

        //page changed lister
        pageChangedListener(viewPager, navigation);

        //get notification count
        CommunApiCalls.countUnseenNotifications(getApplicationContext());

    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(getString(R.string.app_name));
        Tools.setSystemBarColor(this);
    }


    private void initNavigationBottomView() {

        navigation = navigation_bottom_view.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        viewPager.setCurrentItem(0);
                        return true;

                    case R.id.navigation_notification:
                        viewPager.setCurrentItem(1);
                        return true;

                }
                return false;
            }
        });
    }


    private void pageChangedListener(final SwipeDisabledViewPager mViewPager, final BottomNavigationView mBottomNavigationView) {


        mViewPager.addOnPageChangeListener(new SwipeDisabledViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                prevMenuItem = mBottomNavigationView.getMenu().getItem(position);


                if (prevMenuItem != null)
                    prevMenuItem.setChecked(false);
                else {
                    mBottomNavigationView.getMenu().getItem(0).setChecked(false);
                    mBottomNavigationView.getMenu().getItem(position).setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }

        });
    }


    private void initViewPagerAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.search_icon) {

            CustomSearchFragment fragment = new CustomSearchFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .add(R.id.main_container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack("customSearchFrag")
                    .commit();

        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.search_icon).setVisible(true);
        menu.findItem(R.id.share_post).setVisible(false);

        return super.onPrepareOptionsMenu(menu);

    }


    @Override
    public void onBackPressed() {

        final Fragment fragment = getSupportFragmentManager().findFragmentByTag("accountFrag");

        if (fragment != null) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
            viewPager.setCurrentItem(0);
            navigation.setSelectedItemId(R.id.navigation_home);

        }

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void initNavigationMenu() {
        NavigationView nav_view = findViewById(R.id.nav_view);
        final DrawerLayout drawer = findViewById(R.id.main_container);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                Toast.makeText(getApplicationContext(), item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                drawer.closeDrawers();
                return true;
            }
        });

        drawer.closeDrawers();

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        int FRAGS_ITEMS_NUM = 4;

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return FRAGS_ITEMS_NUM;
        }

        // Returns the fragment to display for that page
        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HomeFragment.newInstance(0, "Page # 1");
                case 1:
                    return CategoryFragment.newInstance(1, "Page # 2");
                case 2:
                    if (SessionsController.isLogged())
                        return ProfileFragment.newInstance(2, "Page # 4");
                    else
                        return AuthenticationFragment.newInstance(2, "Page # 3");
                case 3:
                    return NotificationChatVPFragment.newInstance(3, "Page # 4");

                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }


}
