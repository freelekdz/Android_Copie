package com.freelek.apps.freelekdz.booking.views.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.freelek.apps.freelekdz.R;
import com.freelek.apps.freelekdz.appconfig.Constances;
import com.freelek.apps.freelekdz.booking.controllers.CartController;
import com.freelek.apps.freelekdz.booking.modals.Cart;
import com.freelek.apps.freelekdz.booking.modals.Option;
import com.freelek.apps.freelekdz.booking.modals.Service;
import com.freelek.apps.freelekdz.booking.views.activities.BookingCheckoutActivity;
import com.freelek.apps.freelekdz.classes.Store;
import com.freelek.apps.freelekdz.controllers.sessions.SessionsController;
import com.freelek.apps.freelekdz.controllers.stores.StoreController;
import com.freelek.apps.freelekdz.utils.OfferUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;


public class ServiceOptionsFragment extends Fragment {

    @BindView(R.id.frame_content)
    LinearLayout frame_content;

    @BindView(R.id.layout_custom_order)
    LinearLayout layout_custom_order;
    @BindView(R.id.btn_custom_order)
    AppCompatButton btnCustomOrder;
    @BindView(R.id.product_value)
    TextView product_value;
    // custom quantity fields
    private Context mContext;
    private List<Service> mServices;
    private int store_id;
    private Store mStore;
    private float customPrice = 0;

    private RealmList<Service> selectedOptions;
    private Cart mcart;


    @OnClick(R.id.btn_custom_order)
    public void submit(View view) {


        //fill cart detail
        mcart.setModule_id(store_id);
        mcart.setModule(Constances.ModulesConfig.SERVICE_MODULE);
        mcart.setAmount(customPrice);
        mcart.setQte(1);
        mcart.setServices(mStore.getServices());
        mcart.setSelectedService(selectedOptions);
        if (SessionsController.isLogged())
            mcart.setUser_id(SessionsController.getSession().getUser().getId());


        //delete all from carts
        CartController.removeAll();
        //save cart in the database
        CartController.addServiceToCart(mcart);

        //redirect to cart activity
        Intent intent = new Intent(new Intent(getActivity(), BookingCheckoutActivity.class));
        intent.putExtra("module_id", store_id);
        intent.putExtra("module", Constances.ModulesConfig.SERVICE_MODULE);

        startActivity(intent);
        getActivity().finish();

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init custom params
        selectedOptions = new RealmList<>();
        mcart = new Cart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_variant, container, false);
        mContext = root.getContext();
        ButterKnife.bind(this, root);


        Bundle args = getArguments();
        if (args != null) {
            store_id = args.getInt(Constances.ModulesConfig.STORE_MODULE);
            mStore = StoreController.getStore(store_id);

            if (mStore != null) {

                mServices = mStore.getServices();

                if (mServices == null && mServices.size() > 0) {
                    Toast.makeText(getContext(), getString(R.string.no_service_found), Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }

                product_value.setText(String.format(OfferUtils.parseCurrencyFormat(
                        customPrice,
                        OfferUtils.defaultCurrency())));

                generateGroupView(mContext, mServices);

            }
        }


        return root;

    }

    @SuppressLint("StringFormatInvalid")
    private void generateGroupView(Context context, List<Service> services) {

        if (services != null && services.size() > 0) {


            //global fields
            LinearLayout.LayoutParams lp_match_wrap = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams lp_wrap_wrap = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


            for (Service service : services) {

                //fill the selected service
                Service service1 = new Service();
                service1.setGroup_id(service.getGroup_id());

                //group linear layout
                LinearLayout group_wrapper = new LinearLayout(context);
                group_wrapper.setOrientation(LinearLayout.VERTICAL);
                group_wrapper.setPaddingRelative((int) getResources().getDimension(R.dimen.spacing_middle), 0, (int) getResources().getDimension(R.dimen.spacing_middle), (int) getResources().getDimension(R.dimen.spacing_middle));
                LinearLayout.LayoutParams grpLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                grpLayoutParams.setMargins(0, 0, 0, 0);
                group_wrapper.setLayoutParams(grpLayoutParams);

                //group title txt
                TextView group_label = new TextView(context);
                group_label.setText(service.getGroup_label());
                group_label.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                group_label.setTypeface(group_label.getTypeface(), Typeface.BOLD);
                group_label.setTextColor(ContextCompat.getColorStateList(context, R.color.defaultColorText));
                group_label.setTextSize(20);


                lp_match_wrap.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.spacing_medium));

                group_label.setLayoutParams(lp_match_wrap);

                //add group title to the layou
                group_wrapper.addView(group_label);


                if (service.getOptions() != null && service.getOptions().size() > 0) {

                    if (service.getType() != null && service.getType().equalsIgnoreCase(Service.ONE_OPTION)) {


                        Service tempServiceOO = Realm.getDefaultInstance().copyFromRealm(service);



                        /********* ONE_OPTION   *********/
                        final double[] one_option_price = {-1};
                        HashMap<Integer, Double> groupOptionMap = new HashMap<>();


                        for (Option option : service.getOptions()) {

                            LinearLayout service_view_group = new LinearLayout(mContext);
                            service_view_group.setOrientation(LinearLayout.VERTICAL);
                            service_view_group.setLayoutParams(lp_match_wrap);

                            //choice  with price
                            LinearLayout linearLayout_376 = new LinearLayout(mContext);
                            linearLayout_376.setOrientation(LinearLayout.HORIZONTAL);
                            linearLayout_376.setLayoutParams(lp_match_wrap);


                            //radio
                            RadioButton radioBtn = new RadioButton(mContext);
                            LinearLayout.LayoutParams lp_rb = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            lp_rb.weight = 1;
                            radioBtn.setLayoutParams(lp_rb);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                                radioBtn.setButtonTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));

                            //dynamic content
                            radioBtn.setText(option.getLabel());
                            radioBtn.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                            radioBtn.setTag(option.getId());
                            radioBtn.setId(option.getId());

                            //todo: action click listener
                            radioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton view, boolean isChecked) {

                                    Log.e("onCheckedChanged", String.valueOf(isChecked));



                                }
                            });


                            radioBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    //clear radio box
                                    for (int i = 0; i < service.getOptions().size(); i++) {

                                        if(groupOptionMap.containsKey(service.getOptions().get(i).getId())){
                                            customPrice = (float) (customPrice - service.getOptions().get(i).getValue());
                                            //((RadioButton) getView().findViewWithTag(service.getOptions().get(i).getId())).setChecked(false);
                                            ((RadioButton) getView().findViewWithTag(service.getOptions().get(i).getId())).setChecked(false);
                                            groupOptionMap.remove(service.getOptions().get(i).getId());
                                        }

                                    }

                                    for (int i = 0; i < service.getOptions().size(); i++) {


                                        if(
                                                view.getId() == service.getOptions().get(i).getId()
                                        ){
                                            ((RadioButton) getView().findViewWithTag(service.getOptions().get(i).getId())).setChecked(true);
                                            customPrice = (float) (customPrice + service.getOptions().get(i).getValue());
                                            groupOptionMap.put(service.getOptions().get(i).getId(),service.getOptions().get(i).getValue());


                                            tempServiceOO.getOptions().add(option);

                                            RealmList<Option> tempOption = new RealmList<>();
                                            tempOption.add(service.getOptions().get(i));
                                            tempServiceOO.setOptions(tempOption);
                                        }
                                    }


                                    if (tempServiceOO.getOptions() != null && tempServiceOO.getOptions().size() > 0) {
                                        selectedOptions.remove(tempServiceOO);
                                        selectedOptions.add(tempServiceOO);
                                    }


                                    if(customPrice < 0)
                                        customPrice = 0;

                                    product_value.setText(String.format(OfferUtils.parseCurrencyFormat(
                                            customPrice,
                                            OfferUtils.defaultCurrency())));


                                }
                            });

                            linearLayout_376.addView(radioBtn);

                            if (option.getValue() > 0) {

                                LinearLayout.LayoutParams lp_wrap_wrap_price = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                lp_match_wrap.setMargins((int) getResources().getDimension(R.dimen.spacing_medium), 0, 0, (int) getResources().getDimension(R.dimen.spacing_medium));

                                //txt price
                                TextView price_option = new TextView(mContext);
                                price_option.setText(String.format(getContext().getString(R.string.variant_additional_cost), option.getParsed_value()));
                                price_option.setTextColor(getResources().getColor(R.color.colorPrimary));
                                price_option.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                                price_option.setTypeface(price_option.getTypeface(), Typeface.BOLD);
                                price_option.setLayoutParams(lp_wrap_wrap_price);

                                linearLayout_376.addView(price_option);
                            }


                            service_view_group.addView(linearLayout_376);


                            //item description
                            if (option.getDescription() != null && !option.getDescription().trim().equals("")) {
                                //Linear layout checkbox
                                LinearLayout.LayoutParams description_ll_M_M = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                description_ll_M_M.leftMargin = (int) getResources().getDimension(R.dimen.spacing_xlarge);
                                //choice 1 linearlayout
                                LinearLayout description_ll = new LinearLayout(mContext);
                                description_ll.setOrientation(LinearLayout.HORIZONTAL);
                                description_ll.setLayoutParams(description_ll_M_M);

                                //choice 1 price
                                TextView description_txt = new TextView(mContext);
                                description_txt.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                                description_txt.setTextColor(getResources().getColor(R.color.grey_60));
                                description_txt.setTypeface(description_txt.getTypeface(), Typeface.ITALIC);
                                description_txt.setLayoutParams(lp_wrap_wrap);
                                description_ll.addView(description_txt);

                                //dynamic content
                                description_txt.setText(option.getDescription());

                                service_view_group.addView(description_ll);
                            }

                            group_wrapper.addView(service_view_group);


                        }

                    } else if (service.getType() != null && service.getType().equalsIgnoreCase(Service.MULTI_OPTIONS)) {
                        /********* MULTI_OPTIONS   *********/

                        Service serviceMO = Realm.getDefaultInstance().copyFromRealm(service);
                        serviceMO.getOptions().clear();


                        for (Option option : service.getOptions()) {

                            LinearLayout service_view_group_mo = new LinearLayout(mContext);
                            service_view_group_mo.setOrientation(LinearLayout.VERTICAL);
                            service_view_group_mo.setLayoutParams(lp_match_wrap);

                            //Linear layout checkbox
                            LinearLayout.LayoutParams checkBox_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            //choice 1 linearlayout
                            LinearLayout linearLayout_ch_1 = new LinearLayout(mContext);
                            linearLayout_ch_1.setOrientation(LinearLayout.HORIZONTAL);
                            linearLayout_ch_1.setLayoutParams(lp_match_wrap);


                            //choice 1 checkbox
                            CheckBox checkBox = new CheckBox(mContext);
                            checkBox_params.weight = 1;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                                checkBox.setButtonTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));


                            //dynamic content
                            checkBox.setText(option.getLabel());
                            checkBox.setTag(option.getId());
                            checkBox.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                            checkBox.setId(option.getId());


                            //click listener
                            checkBox.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        serviceMO.getOptions().stream()
                                                .filter(option1 -> option1.getId() == option.getId())
                                                .findFirst()
                                                .map(p -> {
                                                    serviceMO.getOptions().remove(p);
                                                    return p;
                                                });
                                    }


                                    if (((CheckBox) view).isChecked()) {

                                        serviceMO.getOptions().add(option);
                                        //calculate the amount
                                        customPrice = (float) (customPrice + option.getValue());

                                    } else {
                                        //calculate the amount
                                        customPrice = (float) (customPrice - option.getValue());
                                    }

                                    if (serviceMO.getOptions() != null && serviceMO.getOptions().size() > 0) {
                                        selectedOptions.remove(serviceMO);
                                        selectedOptions.add(serviceMO);
                                    }

                                    //display the amount on realtime
                                    product_value.setText(OfferUtils.parseCurrencyFormat(
                                            customPrice,
                                            OfferUtils.defaultCurrency()));

                                }
                            });


                            checkBox.setLayoutParams(checkBox_params);

                            linearLayout_ch_1.addView(checkBox);

                            //choice 1 price
                            TextView checkBox_price = new TextView(mContext);

                            checkBox_price.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                            checkBox_price.setTextColor(getResources().getColor(R.color.colorPrimary));
                            checkBox_price.setTypeface(checkBox_price.getTypeface(), Typeface.BOLD);
                            checkBox_price.setLayoutParams(lp_wrap_wrap);
                            linearLayout_ch_1.addView(checkBox_price);
                            service_view_group_mo.addView(linearLayout_ch_1);



                            if (option.getDescription() != null && !option.getDescription().trim().equals("")) {
                                //Linear layout checkbox
                                LinearLayout.LayoutParams description_ll_M_M = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                description_ll_M_M.leftMargin = (int) getResources().getDimension(R.dimen.spacing_xlarge);
                                //choice 1 linearlayout
                                LinearLayout description_ll = new LinearLayout(mContext);
                                description_ll.setOrientation(LinearLayout.HORIZONTAL);
                                description_ll.setLayoutParams(description_ll_M_M);

                                //choice 1 price
                                TextView description_txt = new TextView(mContext);
                                description_txt.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                                description_txt.setTextColor(getResources().getColor(R.color.grey_60));
                                description_txt.setTypeface(description_txt.getTypeface(), Typeface.ITALIC);
                                description_txt.setLayoutParams(lp_wrap_wrap);
                                description_ll.addView(description_txt);


                                //dynamic content
                                description_txt.setText(option.getDescription());

                                service_view_group_mo.addView(description_ll);

                            }

                            if (option.getValue() > 0) {
                                checkBox_price.setText(String.format(getContext().getString(R.string.variant_additional_cost), option.getParsed_value()));
                                checkBox.setVisibility(View.VISIBLE);
                            } else {
                                checkBox_price.setVisibility(View.GONE);
                            }

                            group_wrapper.addView(service_view_group_mo);

                        }


                    }
                }

                frame_content.addView(group_wrapper);
            }


        } else {
            Toast.makeText(context, getString(R.string.no_service_found_for_this_store), Toast.LENGTH_LONG).show();

            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    getActivity().finish();
                }
            }, 1000);

        }

    }


}
