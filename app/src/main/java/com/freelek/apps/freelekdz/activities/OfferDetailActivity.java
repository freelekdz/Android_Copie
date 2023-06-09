package com.freelek.apps.freelekdz.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.freelek.apps.freelekdz.AppController;
import com.freelek.apps.freelekdz.GPS.GPStracker;
import com.freelek.apps.freelekdz.R;
import com.freelek.apps.freelekdz.animation.ImageLoaderAnimation;
import com.freelek.apps.freelekdz.appconfig.AppConfig;
import com.freelek.apps.freelekdz.appconfig.Constances;
import com.freelek.apps.freelekdz.classes.Offer;
import com.freelek.apps.freelekdz.classes.Store;
import com.freelek.apps.freelekdz.controllers.CampagneController;
import com.freelek.apps.freelekdz.controllers.sessions.SessionsController;
import com.freelek.apps.freelekdz.controllers.stores.OffersController;
import com.freelek.apps.freelekdz.controllers.stores.StoreController;
import com.freelek.apps.freelekdz.customView.StoreCardCustomView;
import com.freelek.apps.freelekdz.load_manager.ViewManager;
import com.freelek.apps.freelekdz.network.ServiceHandler;
import com.freelek.apps.freelekdz.network.VolleySingleton;
import com.freelek.apps.freelekdz.network.api_request.SimpleRequest;
import com.freelek.apps.freelekdz.parser.api_parser.OfferParser;
import com.freelek.apps.freelekdz.utils.DateUtils;
import com.freelek.apps.freelekdz.utils.OfferUtils;
import com.freelek.apps.freelekdz.utils.TextUtils;
import com.freelek.apps.freelekdz.utils.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.nirhart.parallaxscroll.views.ParallaxScrollView;
import com.wuadam.awesomewebview.AwesomeWebView;

import org.bluecabin.textoo.LinksHandler;
import org.bluecabin.textoo.Textoo;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

import static com.freelek.apps.freelekdz.appconfig.AppConfig.APP_DEBUG;

public class OfferDetailActivity extends SimpleActivity implements ViewManager.CustomView {


    @BindView(R.id.app_bar)
    Toolbar toolbar;
    @BindView(R.id.badge_category)
    TextView badge_category;
    @BindView(R.id.badge_price)
    TextView badge_price;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.scroll_view)
    ParallaxScrollView scrollView;
    @BindView(R.id.description_content)
    TextView description_content;
    @BindView(R.id.header_title)
    TextView header_title;
    @BindView(R.id.adsLayout)
    LinearLayout adsLayout;

    @BindView(R.id.offer_layout)
    LinearLayout offer_layout;

    @BindView(R.id.adView)
    AdView mAdView;
    @BindView(R.id.offer_up_to)
    TextView offer_up_to;
    @BindView(R.id.customStoreCV)
    StoreCardCustomView customStoreCV;


    private ViewManager mViewManager;
    private int offer_id = 0;
    private Offer offerData;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_detail_offer);

        //Bind views
        ButterKnife.bind(this);

        //setup view manager showError loading content
        setupViewManager();

        //contents of menu have changed, and menu should be redrawn.
        invalidateOptionsMenu();


        //set up views
        setupViews();

        //setup the ADMOB
        setupAdmob();

        //populating data
        listingOffersData();


    }

    @Override
    protected void onResume() {

        if (mAdView != null)
            mAdView.resume();

        super.onResume();
    }

    @Override
    protected void onPause() {

        if (mAdView != null)
            mAdView.pause();

        super.onPause();
    }

    @Override
    protected void onDestroy() {

        if (mAdView != null)
            mAdView.destroy();

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);

        /////////////////////////////
        menu.findItem(R.id.bookmarks_icon).setVisible(false);
        /////////////////////////////
        menu.findItem(R.id.share_post).setVisible(true);
        Drawable send_location = new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon2.cmd_share_variant)
                .color(ResourcesCompat.getColor(getResources(), R.color.white, null))
                .sizeDp(22);
        menu.findItem(R.id.share_post).setIcon(send_location);
        /////////////////////////////

        menu.findItem(R.id.report_icon).setVisible(true);


        return true;
    }

    private void setupViews() {

        //setup toolbar
        setupToolbar(toolbar);
        getAppBarSubtitle().setVisibility(View.GONE);

        //setup scroll with header
        setupScrollNHeader(
                scrollView,
                (LinearLayout) findViewById(R.id.header_detail),
                SimpleHeaderSize.HALF,
                findViewById(R.id.store_detail)
        );


        //setup header views
        setupHeader();

    }

    private void setupHeader() {

        //setup all badge
        setupBadges();

    }

    private void setupBadges() {


        Drawable badge_closed_background = badge_price.getBackground();
        if (badge_closed_background instanceof ShapeDrawable) {
            ((ShapeDrawable) badge_closed_background).getPaint().setColor(ContextCompat.getColor(this, R.color.colorPromo));
        } else if (badge_closed_background instanceof GradientDrawable) {
            ((GradientDrawable) badge_closed_background).setColor(ContextCompat.getColor(this, R.color.colorPromo));
        } else if (badge_closed_background instanceof ColorDrawable) {
            ((ColorDrawable) badge_closed_background).setColor(ContextCompat.getColor(this, R.color.colorPromo));
        }


    }


    private void updateCategoryBadge(String title, String color_hex) {

        badge_category.setVisibility(View.VISIBLE);

        int color = ContextCompat.getColor(this, R.color.colorPrimary);

        try {
            if (color_hex != null && !color_hex.equals("null"))
                color = Color.parseColor(color_hex);
        } catch (Exception e) {
            Log.e("colorParser", e.getMessage());
        }


        Drawable badge_cat_background = badge_category.getBackground();
        if (badge_cat_background instanceof ShapeDrawable) {
            ((ShapeDrawable) badge_cat_background).getPaint().setColor(color);
        } else if (badge_cat_background instanceof GradientDrawable) {
            ((GradientDrawable) badge_cat_background).setColor(color);
        } else if (badge_cat_background instanceof ColorDrawable) {
            ((ColorDrawable) badge_cat_background).setColor(color);
        }

        badge_category.setText(title);

    }


    private void setupViewManager() {

        //setup view manager
        mViewManager = new ViewManager(this);
        mViewManager.setLoadingView(findViewById(R.id.loading));
        mViewManager.setContentView(findViewById(R.id.content));
        mViewManager.setErrorView(findViewById(R.id.error));
        mViewManager.setEmptyView(findViewById(R.id.empty));

        mViewManager.setListener(new ViewManager.CallViewListener() {
            @Override
            public void onContentShown() {
                scrollView.setEnabled(true);
            }

            @Override
            public void onErrorShown() {

            }

            @Override
            public void onEmptyShown() {
                scrollView.setEnabled(false);
            }

            @Override
            public void onLoadingShown() {

            }
        });

        mViewManager.showLoading();

    }


    private void listingOffersData() {

        try {


            //get it from external url (deep linking)
            if (offer_id == 0) {
                try {

                    Intent appLinkIntent = getIntent();
                    String appLinkAction = appLinkIntent.getAction();
                    Uri appLinkData = appLinkIntent.getData();

                    if (appLinkAction != null && appLinkAction.equals(Intent.ACTION_VIEW)) {

                        if (APP_DEBUG)
                            Toast.makeText(getApplicationContext(), appLinkData.toString(), Toast.LENGTH_LONG).show();
                        offer_id = Utils.dp_get_id_from_url(appLinkData.toString(), Constances.ModulesConfig.OFFER_MODULE);

                        if (AppConfig.APP_DEBUG)
                            Log.e("offer_id", offer_id + "");

                        if (AppConfig.APP_DEBUG)
                            Toast.makeText(getApplicationContext(), "The ID: " + offer_id + " " + appLinkAction, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (offer_id == 0) {
                offer_id = getIntent().getExtras().getInt("offer_id");
            }

            if (offer_id == 0) {
                offer_id = getIntent().getExtras().getInt("id");
            }


            if (offer_id == 0) {
                offer_id = Integer.parseInt(Objects.requireNonNull(getIntent().getExtras().getString("id")));
            }

            if (AppConfig.APP_DEBUG)
                Toast.makeText(this, String.valueOf(offer_id), Toast.LENGTH_LONG).show();


        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }


        final Offer mOffer = OffersController.getOffer(offer_id);

        //GET DATA FROM API IF NETWORK IS AVAILABLE
        if (ServiceHandler.isNetworkAvailable(this)) {
            syncOffer(offer_id);
        } else {
            if (mOffer != null && mOffer.isLoaded() && mOffer.isValid()) {

                mViewManager.showContent();
                setupComponents(mOffer);
                offerData = mOffer;
            }
        }



        /*
         *
         *   DATE & COUNTDOWN
         *
         */

        String date = "";


        try {
            date = mOffer.getDate_start();
            date = DateUtils.prepareOutputDate(date, "dd MMMM yyyy  hh:mm", this);
        } catch (Exception e) {

            syncOffer(offer_id);
            return;

        }

    }


    private void setupComponents(final Offer mOffer) {
        offerData = mOffer;

        getAppBarTitle().setText(mOffer.getName());
        header_title.setText(mOffer.getName());


        if (mOffer.getValue_type().equalsIgnoreCase("Percent") && (mOffer.getOffer_value() > 0 || mOffer.getOffer_value() < 0)) {
            DecimalFormat decimalFormat = new DecimalFormat("#0");
            badge_price.setText(decimalFormat.format(mOffer.getOffer_value()) + "%");
        } else {
            if (mOffer.getValue_type().equalsIgnoreCase("Price") && mOffer.getOffer_value() != 0) {

                badge_price.setText(OfferUtils.parseCurrencyFormat(
                        mOffer.getOffer_value(),
                        mOffer.getCurrency()));

            } else {
                badge_price.setText(getString(R.string.promo));
            }
        }

        badge_price.setVisibility(View.VISIBLE);

        if (mOffer.getImages() != null)
            Glide.with(getBaseContext()).load(mOffer.getImages().getUrl500_500())
                    .centerCrop()
                    .placeholder(ImageLoaderAnimation.glideLoader(this))
                    .into(image);


        description_content.setText(mOffer.getDescription());
        new TextUtils.decodeHtml(description_content).execute(mOffer.getDescription());

        Textoo
                .config(description_content)
                .linkifyWebUrls()  // or just .linkifyAll()
                .addLinksHandler(new LinksHandler() {
                    @Override
                    public boolean onClick(View view, String url) {

                        if (Utils.isValidURL(url)) {

                            new AwesomeWebView.Builder(OfferDetailActivity.this)
                                    .showMenuOpenWith(false)
                                    .statusBarColorRes(R.color.colorAccent)
                                    .theme(R.style.FinestWebViewAppTheme)
                                    .titleColor(
                                            ResourcesCompat.getColor(getResources(), R.color.white, null)
                                    ).urlColor(
                                    ResourcesCompat.getColor(getResources(), R.color.white, null)
                            ).show(url);

                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .apply();

        try {

            int cid = Integer.parseInt(getIntent().getExtras().getString("cid"));
            CampagneController.markView(cid);
            // Toast.makeText(this,"CMarkViewClicked",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            if (AppConfig.APP_DEBUG)
                e.printStackTrace();
        }

        //setup offer date
        setupOfferDate(mOffer);


        if (mOffer.getStore_id() > 0 && StoreController.getStore(mOffer.getStore_id()) != null) {
            Store storeOffers = StoreController.getStore(mOffer.getStore_id());
            customStoreCV.setupComponent(storeOffers);
            updateCategoryBadge(storeOffers.getCategory_name(), storeOffers.getCategory_color());

        } else {

            customStoreCV.loadData(mOffer.getStore_id(), false, new StoreCardCustomView.StoreListener() {
                @Override
                public void onLoaded(Store object) {
                    updateCategoryBadge(object.getCategory_name(), object.getCategory_color());
                }
            });

        }


    }

    @SuppressLint("StringFormatMatches")
    private void setupOfferDate(Offer mOffer) {

        String dateStartAt = "";
        String dateEndAt = "";

        try {
            dateStartAt = mOffer.getDate_start();
            dateStartAt = DateUtils.prepareOutputDate(dateStartAt, "dd MMMM yyyy", this);
        } catch (Exception e) {
            return;
        }

        try {
            dateEndAt = mOffer.getDate_end();
            dateEndAt = DateUtils.prepareOutputDate(dateEndAt, "dd MMMM yyyy", this);
        } catch (Exception e) {
            return;
        }

        //        if(mOffer.getType()==0){
//            CountdownView mCvCountdownView = (CountdownView)findViewById(R.id.cv_countdownViewTest1);
//            mCvCountdownView.setVisibility(View.GONE);
//            typeView.setVisibility(View.GONE);
//        }else {

        String inputDateSatrt = DateUtils.prepareOutputDate(mOffer.getDate_start(), "yyyy-MM-dd HH:mm:ss", this);
        long diff_Will_Start = DateUtils.getDiff(inputDateSatrt, "yyyy-MM-dd HH:mm:ss");

        if (AppConfig.APP_DEBUG) {

            Log.e("_start_at_server", mOffer.getDate_start());
            Log.e("_start_at_device ", dateStartAt);
            Log.e("_start_at_diff ", String.valueOf(diff_Will_Start));
        }


        if (diff_Will_Start > 0) {
            offer_up_to.setText(String.format(getString(R.string.offer_start_at), dateStartAt));

            if (dateStartAt != null && dateStartAt.equals("null")) {
                offer_layout.setVisibility(View.GONE);
            }

        }


        String inputDateEnd = DateUtils.prepareOutputDate(mOffer.getDate_end(), "yyyy-MM-dd HH:mm:ss", this);
        long diff_will_end = DateUtils.getDiff(inputDateEnd, "yyyy-MM-dd HH:mm:ss");


        if (AppConfig.APP_DEBUG) {
            Log.e("_end_at_server", mOffer.getDate_end());
            Log.e("_end_at_device ", dateEndAt);
            Log.e("_end_at_diff ", String.valueOf(diff_will_end));

        }


        if (diff_will_end > 0 && diff_Will_Start < 0) {
            if (dateEndAt == null || dateEndAt.equals("null")) {
                offer_layout.setVisibility(View.GONE);
            } else {
                offer_up_to.setText(String.format(getString(R.string.offer_end_at), dateEndAt));
                offer_layout.setVisibility(View.VISIBLE);
            }
        }


        if (diff_Will_Start < 0 && diff_will_end < 0) {
            offer_layout.setVisibility(View.VISIBLE);
            offer_up_to.setText(String.format(getString(R.string.offer_ended_at), dateEndAt));
        }

        // typeView.setVisibility(View.VISIBLE);


//        }
//

        Drawable offerCalendar = new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_calendar)
                .color(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null))
                .sizeDp(18);
        offer_up_to.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        if (AppController.isRTL()) {
            offer_up_to.setCompoundDrawables(null, null, offerCalendar, null);
        } else {
            offer_up_to.setCompoundDrawables(offerCalendar, null, null, null);
        }

        offer_up_to.setCompoundDrawablePadding(20);


    }


    private void syncOffer(final int offer_id) {

        mViewManager.showLoading();

        final GPStracker mGPS = new GPStracker(this);

        SimpleRequest request = new SimpleRequest(Request.Method.POST,
                Constances.API.API_GET_OFFERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    if (APP_DEBUG) {
                        Log.e("responseOffersString", response);
                    }

                    JSONObject jsonObject = new JSONObject(response);
                    final OfferParser mOfferParser = new OfferParser(jsonObject);
                    RealmList<Offer> list = mOfferParser.getOffers();

                    if (list.size() > 0) {

                        mViewManager.showContent();

                        OffersController.insertOffers(list);
                        setupComponents(list.get(0));

                    } else {

                        Toast.makeText(OfferDetailActivity.this, getString(R.string.store_not_found), Toast.LENGTH_LONG).show();
                        finish();

                    }

                } catch (JSONException e) {
                    //send a rapport to support
                    if (APP_DEBUG)
                        e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (APP_DEBUG) {
                    Log.e("ERROR", error.toString());
                }
                mViewManager.showError();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (mGPS.canGetLocation()) {
                    params.put("lat", mGPS.getLatitude() + "");
                    params.put("lng", mGPS.getLongitude() + "");
                }

                params.put("limit", "1");
                params.put("offer_id", offer_id + "");

                if (APP_DEBUG) {
                    Log.e("ListStoreFragment", "  params getOffers :" + params.toString());
                }

                return params;
            }

        };


        request.setRetryPolicy(new DefaultRetryPolicy(SimpleRequest.TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(this).getRequestQueue().add(request);

    }


    private void setupAdmob() {

        if (AppConfig.SHOW_ADS && AppConfig.SHOW_ADS_IN_STORE) {

            mAdView = findViewById(R.id.adView);
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();

            mAdView.loadAd(adRequest);
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }

                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    // Code to be executed when an ad request fails.
                    mAdView.setVisibility(View.GONE);
                    findViewById(R.id.adsLayout).setVisibility(View.GONE);
                }

                @Override
                public void onAdImpression() {
                    // Code to be executed when an impression is recorded
                    // for an ad.
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mAdView.setVisibility(View.VISIBLE);
                    findViewById(R.id.adsLayout).setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }
            });

            mAdView.loadAd(adRequest);


        } else
            findViewById(R.id.adsLayout).setVisibility(View.GONE);




    }


    @Override
    public void customErrorView(View v) {

    }

    @Override
    public void customLoadingView(View v) {

    }

    @Override
    public void customEmptyView(View v) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (android.R.id.home == item.getItemId()) {
            if (!MainActivity.isOpend()) {
                startActivity(new Intent(this, MainActivity.class));
            }
            finish();
        } else if (item.getItemId() == R.id.map_action) {

            startActivity(new Intent(this, MapStoresListActivity.class));
            overridePendingTransition(R.anim.lefttoright_enter, R.anim.lefttoright_exit);

        } else if (item.getItemId() == R.id.share_post) {


            @SuppressLint({"StringFormatInvalid", "LocalSuppress", "StringFormatMatches"}) String shared_text =
                    String.format(getString(R.string.shared_text),
                            offerData.getName(),
                            getString(R.string.app_name),
                            offerData.getLink()
                    );

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shared_text);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } else if(item.getItemId() == R.id.report_icon){

            if(!SessionsController.isLogged()){
                startActivity(new Intent(OfferDetailActivity.this, CustomSearchActivity.LoginActivityV2.class));
                overridePendingTransition(R.anim.lefttoright_enter, R.anim.lefttoright_exit);
            }else{

                Intent intent = new Intent(OfferDetailActivity.this, ReportIssueActivity.class);
                intent.putExtra("id", offerData.getId());
                intent.putExtra("name", offerData.getName());
                intent.putExtra("link", offerData.getLink());
                intent.putExtra("owner_id", offerData.getUser_id());
                intent.putExtra("module", "offer");
                startActivity(intent);
            }



        }


        return super.onOptionsItemSelected(item);

    }


}
