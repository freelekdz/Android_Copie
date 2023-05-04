package com.freelek.apps.freelekdz.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.balysv.materialripple.MaterialRippleLayout;
import com.freelek.apps.freelekdz.GPS.GPStracker;
import com.freelek.apps.freelekdz.R;
import com.freelek.apps.freelekdz.appconfig.AppConfig;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindMyPlaceActivity extends AppCompatActivity implements OnMapReadyCallback {

    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_subtitle)
    TextView toolbarDescription;

    @BindView(R.id.confirm_btn)
    MaterialRippleLayout confirmBtn;

    @BindView(R.id.default_marker)
    ImageView defaultMarker;


    private GoogleMap mMap;
    private final String TAG = "FindMyPlaceActivity";
    private double lat = -1, lng = -1;
    private String address = "unspecified";
    private GPStracker trackMe;
    private LatLng myPosition;

    @OnClick(R.id.confirm_btn)
    public void submit(View view) {

        if (lat == -1 && lng == -1) {
            Toast.makeText(this, "Location isn't selected", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        intent.putExtra("address", address);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_my_place);
        ButterKnife.bind(this);

        initPlacesAPi();

        initToolbar();

        initAutoCompleteSection();

        //INITIALIZE MY LOCATION
        getLocationFromGPSTracker();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private void getLocationFromGPSTracker() {
        trackMe = new GPStracker(this);
        lat = trackMe.getLatitude();
        lng = trackMe.getLongitude();
        myPosition = new LatLng(lat, lng);
    }

    public void initToolbar() {

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarDescription.setVisibility(View.GONE);
        toolbarTitle.setText(R.string.find_my_place);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 16));
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                lat = mMap.getCameraPosition().target.latitude;
                lng = mMap.getCameraPosition().target.longitude;

                if (AppConfig.APP_DEBUG)
                    Log.e("CurrentLocation", lat + " , " + lng);
                // mMap.addMarker(new MarkerOptions().position(mMap.getCameraPosition().target).title("My location").draggable(true));
            }
        });

    }


    private void initPlacesAPi() {


        String apiKey = getString(R.string.places_api_key);

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

    }


    private void initAutoCompleteSection() {

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16));

                lat = place.getLatLng().latitude;
                lng = place.getLatLng().longitude;
                address = place.getAddress();


            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (android.R.id.home == item.getItemId()) {
            Intent intent = new Intent();

            setResult(Activity.RESULT_CANCELED, intent);
            finish();
            overridePendingTransition(R.anim.righttoleft_enter, R.anim.righttoleft_exit);
        }

        return super.onOptionsItemSelected(item);
    }


}