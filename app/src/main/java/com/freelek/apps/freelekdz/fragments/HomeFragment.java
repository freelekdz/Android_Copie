package com.freelek.apps.freelekdz.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.freelek.apps.freelekdz.R;
import com.freelek.apps.freelekdz.Services.BusStation;
import com.freelek.apps.freelekdz.activities.CustomSearchActivity;
import com.freelek.apps.freelekdz.activities.EventsListActivity;
import com.freelek.apps.freelekdz.activities.OffersListActivity;
import com.freelek.apps.freelekdz.activities.PeopleListActivity;
import com.freelek.apps.freelekdz.appconfig.AppConfig;
import com.freelek.apps.freelekdz.appconfig.Constances;
import com.freelek.apps.freelekdz.customView.CategoryCustomView;
import com.freelek.apps.freelekdz.customView.EventCustomView;
import com.freelek.apps.freelekdz.customView.OfferCustomView;
import com.freelek.apps.freelekdz.customView.PeopleCustomView;
import com.freelek.apps.freelekdz.customView.SliderCustomView;
import com.freelek.apps.freelekdz.customView.StoreCustomView;
import com.freelek.apps.freelekdz.helper.AppHelper;
import com.freelek.apps.freelekdz.navigationdrawer.NavigationDrawerFragment;

import java.util.HashMap;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public final static String TAG = "homefragment";

    //binding
    @BindView(R.id.mScroll)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;


    private View rootview;
    //private SwipeRefreshLayout swipeRefreshLayout;
    private String[] header_bg_list = {
            "header_home_bg/header_bg_1.png",
            "header_home_bg/header_bg_1.png",
            "header_home_bg/header_bg_1.png",
    };
    private Listener mListener;

    // newInstance constructor for creating fragment with arguments
    public static HomeFragment newInstance(int page, String title) {
        HomeFragment fragmentFirst = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt("id", page);
        args.putString("title", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }


    private void setupRefresListener() {
        refresh.setOnRefreshListener(this);
        refresh.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorAccent,
                R.color.colorAccent,
                R.color.colorAccent
        );
    }

    private void setup_header(View rootview) {

        AppCompatImageView header_bg = rootview.findViewById(R.id.header_bg);

        Random Dice = new Random();
        int n = Dice.nextInt(header_bg_list.length);

        Glide.with(this)
                .load(AppHelper.loadDrawable(getActivity(), header_bg_list[n]))
                .centerCrop().into(header_bg);

        ImageButton navigation = rootview.findViewById(R.id.navigation);
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusStation.getBus().post(new NavigationDrawerFragment.NavigationDrawerEvent(1));
            }
        });

        rootview.findViewById(R.id.lbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CustomSearchActivity.class));
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.v2_fragment_home, container, false);

        rootview = rootView.getRootView();

        initCategoryRV(rootview);

        initSliderCustomView(rootview);

        initStoreRV(rootview);

        initFeaturedStores(rootview);

        initOfferRV(rootview);

        initEventRv(rootview);

        initPeopleRV(rootview);


        ButterKnife.bind(this, rootview);

        setupScroll();

        setupRefresListener();

        setup_header(rootview);


        return rootview;
    }


    @Override
    public void onStart() {
        super.onStart();

    }



    private void setupScroll() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mNestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    Log.e(getTag(), scrollX + " - " + scrollY);

                    if (mListener != null)
                        mListener.onScroll(scrollX, scrollY);
                }
            });
        }
    }

    private void initSliderCustomView(View view) {
        SliderCustomView mSliderCustomView = view.findViewById(R.id.sliderCV);
        mSliderCustomView.loadData(false);
        mSliderCustomView.startAutoSlider();

        mSliderCustomView.show();
    }

    private void initCategoryRV(View view) {
        //ImageSlider
        CategoryCustomView mCategoryCustomView = view.findViewById(R.id.rectCategoryList);
        mCategoryCustomView.loadData(false);
        mCategoryCustomView.show();
    }

    private void initStoreRV(View view) {
        StoreCustomView mStoreCustomView = view.findViewById(R.id.horizontalStoreList);
        mStoreCustomView.loadData(false, new HashMap<>());

        mStoreCustomView.show();
    }


    private void initFeaturedStores(View view) {

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {

                StoreCustomView mStoreCustomView = view.findViewById(R.id.horizontalFeaturedStores);
                HashMap<String, Object> optionalParams = new HashMap<>();
                optionalParams.put("order_by", Constances.OrderByFilter.TOP_RATED);

                mStoreCustomView.loadData(false, optionalParams);
                mStoreCustomView.show();
            }
        }, 2000);

    }

    private void initOfferRV(View view) {
        OfferCustomView mOfferCustomView = view.findViewById(R.id.horizontalOfferList);
        mOfferCustomView.loadData(false, new HashMap<>());
        view.findViewById(R.id.card_show_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OffersListActivity.class));
                getActivity().overridePendingTransition(R.anim.lefttoright_enter, R.anim.lefttoright_exit);
            }
        });

        mOfferCustomView.show();

    }

    private void initPeopleRV(View view) {
        PeopleCustomView mPeopleCustomView = view.findViewById(R.id.horizontalPeopleList);
        mPeopleCustomView.loadData(false);
        view.findViewById(R.id.card_show_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PeopleListActivity.class));
                getActivity().overridePendingTransition(R.anim.lefttoright_enter, R.anim.lefttoright_exit);
            }
        });

        if (AppConfig.ENABLE_PEOPLE_AROUND_ME) {
            mPeopleCustomView.show();
        } else {
            mPeopleCustomView.hide();
        }


    }

    private void initEventRv(View view) {
        EventCustomView mEventCustomView = view.findViewById(R.id.horizontalEventList);
        mEventCustomView.loadData(false);
        view.findViewById(R.id.card_show_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EventsListActivity.class));
                getActivity().overridePendingTransition(R.anim.lefttoright_enter, R.anim.lefttoright_exit);
            }
        });

        mEventCustomView.show();
    }

    @Override
    public void onRefresh() {

        setup_header(rootview);

        initCategoryRV(rootview);

        initSliderCustomView(rootview);

        initStoreRV(rootview);

        initFeaturedStores(rootview);

        initOfferRV(rootview);

        initEventRv(rootview);

        initPeopleRV(rootview);

        refresh.setRefreshing(false);

    }

    public void setListener(final Listener mItemListener) {
        this.mListener = mItemListener;
    }

    public interface Listener {
        void onScroll(int scrollX, int scrollY);
    }


}
