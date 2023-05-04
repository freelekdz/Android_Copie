package com.freelek.apps.freelekdz.booking.views.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.freelek.apps.freelekdz.R;
import com.freelek.apps.freelekdz.activities.StoreDetailActivity;
import com.freelek.apps.freelekdz.booking.controllers.OrdersController;
import com.freelek.apps.freelekdz.booking.controllers.restApis.OrderApis;
import com.freelek.apps.freelekdz.booking.modals.Item;
import com.freelek.apps.freelekdz.booking.modals.Reservation;
import com.freelek.apps.freelekdz.classes.Store;
import com.freelek.apps.freelekdz.controllers.SettingsController;
import com.freelek.apps.freelekdz.utils.DateUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookingDetailActivity extends AppCompatActivity implements OrderApis.OrderRestAPisDelegate {

    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_subtitle)
    TextView toolbarDescription;

    @BindView(R.id.booking_id)
    TextView order_id;
    @BindView(R.id.delivery_on)
    TextView delivery_on;


    @BindView(R.id.services)
    TextView services;

    @BindView(R.id.items_wrapper)
    LinearLayout item_wrapper;

    @BindView(R.id.store_name)
    TextView store_name;
    @BindView(R.id.owner_address)
    TextView owner_address;


    @BindView(R.id.contact_btn_owner)
    AppCompatButton contact_btn_owner;


    @BindView(R.id.detail_btn_owner)
    AppCompatButton detail_btn_owner;


    @BindView(R.id.order_status)
    TextView order_status;


    private Reservation mReservation;
    private OrderApis call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_detail_fragment);
        ButterKnife.bind(this);

        initToolbar();

        //delegate a listener to retrieve data
        call = OrderApis.newInstance();
        call.delegate = this;

        retrieveDataFromOrder();

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (android.R.id.home == item.getItemId()) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void retrieveDataFromOrder() {

        if (getIntent() != null && getIntent().hasExtra("id"))
            mReservation = OrdersController.findOrderById(getIntent().getExtras().getInt("id"));

        if (mReservation != null) {


            toolbarTitle.setText(String.format(getString(R.string.booking_detail),"#" + mReservation.getId()));

            String inputDate = DateUtils.prepareOutputDate(mReservation.getCreated_at(), "dd MMMM yyyy  hh:mm", this);
            order_id.setText("#" + mReservation.getId());
            delivery_on.setText(inputDate);

            //set status with color
            String[] arrayStatus = mReservation.getStatus().split(";");
            if (arrayStatus.length > 0) {
                order_status.setText(arrayStatus[0].substring(0, 1).toUpperCase() + arrayStatus[0].substring(1));
                if (arrayStatus[1] != null && !arrayStatus[0].equals("null")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        order_status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(arrayStatus[1])));
                    }
                }
            }


            if (mReservation.getItems() != null && mReservation.getItems().size() > 0) {

                String servicesTxt = "";
                for (Item item : mReservation.getItems()) {

                    servicesTxt = servicesTxt + item.getName() + " \n";


                    String[] arrayServices = item.getService().split(",");

                    if (arrayServices.length > 0) {
                        for (String serv : arrayServices) {

                            servicesTxt = servicesTxt + " - \t " + serv + " \n  ";

                        }
                        servicesTxt = servicesTxt + " \n";
                    }
                }
                services.setText(servicesTxt);
                item_wrapper.setVisibility(View.VISIBLE);
            } else {
                item_wrapper.setVisibility(View.GONE);
            }


            //call apis to retrieve store and customer detail from ids
            HashMap paramsStoreAPI = new HashMap<>();
            paramsStoreAPI.put("store_id", String.valueOf(mReservation.getId_store()));
            call.getStoreDetail(paramsStoreAPI);

        }

    }

    public void initToolbar() {

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarDescription.setVisibility(View.GONE);
    }


    @Override
    public void onStoreSuccess(Store storeData) {

        store_name.setText(storeData.getName());
        owner_address.setText(storeData.getAddress());

        //button click listener
        contact_btn_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = storeData.getPhone();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber.trim()));
                if (ActivityCompat.checkSelfPermission(BookingDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    String[] permission = new String[]{Manifest.permission.CALL_PHONE};
                    SettingsController.requestPermissionM(BookingDetailActivity.this, permission);
                    return;
                }
                startActivity(intent);

            }
        });

        detail_btn_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(BookingDetailActivity.this, StoreDetailActivity.class));
                intent.putExtra("id", storeData.getId());
                startActivity(intent);
                overridePendingTransition(R.anim.lefttoright_enter, R.anim.lefttoright_exit);
            }
        });

    }


    @Override
    public void onError(OrderApis object, Map<String, String> errors) {

    }
}
