package com.freelek.apps.freelekdz.booking.views.fragments.checkout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.freelek.apps.freelekdz.R;
import com.freelek.apps.freelekdz.activities.FindMyPlaceActivity;
import com.freelek.apps.freelekdz.booking.modals.CF;
import com.freelek.apps.freelekdz.booking.views.activities.BookingCheckoutActivity;
import com.freelek.apps.freelekdz.controllers.sessions.SessionsController;
import com.freelek.apps.freelekdz.utils.Utils;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.HashMap;

import static android.widget.LinearLayout.HORIZONTAL;
import static com.freelek.apps.freelekdz.AppController.getInstance;
import static com.freelek.apps.freelekdz.booking.views.activities.BookingCheckoutActivity.mStore;
import static com.freelek.apps.freelekdz.booking.views.activities.BookingCheckoutActivity.orderFields;


public class BookingInfoFragment extends Fragment {

    public static int AUTOCOMPLETE_REQUEST_CODE = 1001;
    public static int REQUEST_LOCATION_LAT_LNG = 2002;

    private static int counter = 1;
    private View root;


    public BookingInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_order_info, container, false);

        parserInputViews(root);

        return root;
    }


    private void generateIncrementerLayout(final CF mCF, final LinearLayout container, final Context context) {


        LinearLayout view_wrapper = new LinearLayout(context);
        view_wrapper.setGravity(Gravity.CENTER_VERTICAL);
        view_wrapper.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layout_339 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        view_wrapper.setLayoutParams(layout_339);

        String txtGrpLabel = mCF.getLabel();

        if (mCF.getRequired() == 1) {
            txtGrpLabel = txtGrpLabel + "*";
        }

        //group title txt
        TextView group_label = new TextView(context);
        group_label.setText(txtGrpLabel);
        group_label.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        group_label.setTypeface(group_label.getTypeface(), Typeface.BOLD);
        group_label.setTextSize(11);
        group_label.setTextColor(getResources().getColor(R.color.defaultColorText));
        group_label.setTextColor(ContextCompat.getColorStateList(context, R.color.grey_20));

        LinearLayout.LayoutParams group_label_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        group_label_lp.setMargins(0, (int) getResources().getDimension(R.dimen.spacing_large), 0, 0);
        group_label.setLayoutParams(group_label_lp);


        view_wrapper.addView(group_label);


        LinearLayout number_counter = new LinearLayout(context);
        LinearLayout.LayoutParams layout_576 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout_576.setMargins(0, (int) getResources().getDimension(R.dimen.spacing_large), 0, 0);
        number_counter.setOrientation(HORIZONTAL);
        number_counter.setPaddingRelative((int) getResources().getDimension(R.dimen.spacing_large), 0, 0, 0);
        number_counter.setLayoutParams(layout_576);

        ImageView btn_less_qte = new ImageView(context);
        btn_less_qte.setBackgroundResource(R.color.color_message_layout);
        btn_less_qte.setPaddingRelative((int) getResources().getDimension(R.dimen.spacing_small), 0, 0, 0);
        btn_less_qte.setImageResource(R.drawable.ic_remove);
        btn_less_qte.setColorFilter(getResources().getColor(R.color.defaultColorText));
        LinearLayout.LayoutParams layout_138 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        btn_less_qte.setLayoutParams(layout_138);

        number_counter.addView(btn_less_qte);


        TextView number_person = new TextView(context);
        number_person.setBackgroundResource(R.color.color_message_layout);
        number_person.setPadding((int) getResources().getDimension(R.dimen.spacing_small), (int) getResources().getDimension(R.dimen.spacing_small), (int) getResources().getDimension(R.dimen.spacing_small), (int) getResources().getDimension(R.dimen.spacing_small));
        number_person.setText("1");
        number_person.setTextColor(getResources().getColor(R.color.defaultColorText));
        number_person.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        number_person.setTypeface(number_person.getTypeface(), Typeface.BOLD);

        number_counter.addView(number_person);


        ImageView btn_more_qte = new ImageView(context);
        btn_more_qte.setBackgroundResource(R.color.color_message_layout);
        btn_more_qte.setPaddingRelative((int) getResources().getDimension(R.dimen.spacing_small), 0, 0, 0);
        btn_more_qte.setImageResource(R.drawable.ic_add);
        btn_more_qte.setColorFilter(getResources().getColor(R.color.defaultColorText));
        LinearLayout.LayoutParams layout_735 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        btn_more_qte.setLayoutParams(layout_735);

        number_counter.addView(btn_more_qte);

        LinearLayout.LayoutParams layout_576_ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layout_576_ll.setMargins((int) getResources().getDimension(R.dimen.spacing_middle), 0, (int) getResources().getDimension(R.dimen.spacing_middle), 0);
        layout_576_ll.weight = 1;
        number_person.setLayoutParams(layout_576_ll);


        view_wrapper.addView(number_counter);

        container.addView(view_wrapper);

        //click listener
        btn_more_qte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;
                number_person.setText(counter + "");
                orderFields.put(mCF.getLabel(), counter + "");
            }
        });

        btn_less_qte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter--;
                number_person.setText(counter + "");
                orderFields.put(mCF.getLabel(), counter + "");
            }
        });

        //init people number
        orderFields.put(mCF.getLabel(), counter + "");


    }

    @SuppressLint({"ResourceType", "ClickableViewAccessibility"})
    private void parserInputViews(View view) {


        LinearLayout itemWrapper = view.findViewById(R.id.item_wrapper);

        if (mStore != null) {

            int userId = SessionsController.getSession().getUser().getId();
            int cfId = mStore.getCf_id();
            SharedPreferences saveCF = getInstance().getSharedPreferences("savedCF_" + cfId + "_" + userId, Context.MODE_PRIVATE);


            if (saveCF != null) {
                //get saved custom field from shared  pref
                if (saveCF.getInt("user_id", 0) == userId && saveCF.getInt("req_cf_id", 0) == cfId) {
                    Type type = new TypeToken<HashMap<String, String>>() {
                    }.getType();
                    Gson gson = new Gson();
                    orderFields = gson.fromJson(saveCF.getString("cf", null), type);
                }
            }

            if (orderFields == null) {
                orderFields = new HashMap<String, String>();
            }

            for (CF mCF : mStore.getCf()) {
                if (mCF.getType() != null) {
                    //List<String> arrayType = Arrays.asList(mCF.getType().split("."));
                    String[] arrayType = mCF.getType().split("\\.");
                    if (arrayType.length > 0 && (arrayType[0].equals("input") || arrayType[0].equals("textarea"))) {


                        TextInputLayout txtInpLayout = new TextInputLayout(view.getContext());
                        txtInpLayout.setHintTextAppearance(R.style.cf_et_style);

                        LinearLayout.LayoutParams TILlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        txtInpLayout.setLayoutParams(TILlp);

                        AppCompatEditText et = new AppCompatEditText(view.getContext());
                        //underline edittext
                        //et.setBackgroundResource(ResourcesCompat.getColor(getContext().getResources(), R.color.grey_40, null));

                        //set data if exist
                        if (orderFields.containsKey(mCF.getLabel()) && orderFields.get(mCF.getLabel()) != null) {
                            et.setText(orderFields.get(mCF.getLabel()));
                            et.setVisibility(View.GONE);
                        } else {
                            orderFields.put(mCF.getLabel(), "");

                        }

                        // setting input type filter
                        if (arrayType[1].equals("number")) {
                            generateIncrementerLayout(mCF, itemWrapper, getContext());
                            return;
                        } else if (arrayType[1].equals("text")) {
                            et.setInputType(InputType.TYPE_CLASS_TEXT);
                        } else if (arrayType[1].equals("phone")) {
                            et.setInputType(InputType.TYPE_CLASS_PHONE);
                        } else if (arrayType[1].equals("date")) {

                            et.setClickable(true);
                            et.setFocusable(false);
                            et.setInputType(InputType.TYPE_NULL);

                            et.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //init field
                                    int mYear, mMonth, mDay;


                                    // Get Current Date
                                    final Calendar c = Calendar.getInstance();
                                    mYear = c.get(Calendar.YEAR);
                                    mMonth = c.get(Calendar.MONTH);
                                    mDay = c.get(Calendar.DAY_OF_MONTH);


                                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                            new DatePickerDialog.OnDateSetListener() {

                                                @Override
                                                public void onDateSet(DatePicker view, int year,
                                                                      int monthOfYear, int dayOfMonth) {
                                                    et.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                                }
                                            }, mYear, mMonth, mDay);
                                    datePickerDialog.show();

                                }
                            });

                        } else if (arrayType[1].equals("time")) {

                            et.setClickable(true);
                            et.setFocusable(false);
                            et.setInputType(InputType.TYPE_NULL);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21
                                et.setShowSoftInputOnFocus(false);
                            } else { // API 11-20
                                et.setTextIsSelectable(true);
                            }


                            //add istener
                            et.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    //init params
                                    int mHour, mMinute;
                                    // Get Current Time
                                    final Calendar c = Calendar.getInstance();
                                    mHour = c.get(Calendar.HOUR_OF_DAY);
                                    mMinute = c.get(Calendar.MINUTE);

                                    // Launch Time Picker Dialog
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    et.setText(String.format("%02d:%02d:00", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute));
                                                }
                                            }, mHour, mMinute, false);
                                    timePickerDialog.show();
                                }
                            });

                        } else if (arrayType[1].equals("location")) {

                            //set the marker when location is changed
                            Drawable locationDrawable = new IconicsDrawable(getContext())
                                    .icon(CommunityMaterial.Icon.cmd_crosshairs_gps)
                                    .color(ResourcesCompat.getColor(getContext().getResources(), R.color.colorAccent, null))
                                    .sizeDp(18);

                            et.setTag(mCF.getLabel());
                            TooltipCompat.setTooltipText(et, getResources().getString(R.string.click_marker_to_pick_location));

                            et.setCompoundDrawables(null, null, locationDrawable, null);
                            et.setCompoundDrawablePadding(4);

                            et.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    final int DRAWABLE_RIGHT = 2;
                                    if (event.getAction() == MotionEvent.ACTION_UP) {
                                        if (event.getRawX() >= (et.getRight() - et.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                            Intent intent = new Intent(getActivity(), FindMyPlaceActivity.class);
                                            startActivityForResult(intent, REQUEST_LOCATION_LAT_LNG);
                                            return true;
                                        }
                                    }
                                    return false;
                                }
                            });


                            if (et.getText() != null && et.getText().toString().trim().length() > 0) {
                                String[] arrayLocation = et.getText().toString().split(";");
                                if (arrayLocation.length > 0) {
                                    et.setText(arrayLocation[0]);
                                }

                            }

                        }


                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(Utils.dpToPx(5), Utils.dpToPx(5), Utils.dpToPx(5), Utils.dpToPx(5));


                        String fieldName = mCF.getLabel();
                        if (mCF.getRequired() == 1) {
                            fieldName = fieldName + "*";
                        }
                        et.setHint(fieldName);


                        //set view listener :
                        et.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                                String address = null;
                                String[] arrayType = mCF.getType().split("\\.");

                                if (arrayType[1].equals("location") && orderFields.containsKey(mCF.getLabel()) && !orderFields.get(mCF.getLabel()).isEmpty()) {
                                    address = orderFields.get(mCF.getLabel());
                                    String[] parsedAdr = address.split(";");

                                    if (parsedAdr.length == 3) {
                                        address = s.toString() + ";" + parsedAdr[1] + ";" + parsedAdr[2];
                                    } else {
                                        address = s.toString();
                                    }
                                } else {
                                    address = s.toString();
                                }

                                orderFields.put(mCF.getLabel(), address);


                            }
                        });

                        et.setVisibility(View.VISIBLE);

                        txtInpLayout.addView(et);
                        itemWrapper.addView(txtInpLayout);


                    }
                }
            }

        }


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == AutocompleteActivity.RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("CustomSearchFrag", "Place: " + place.getName() + ", " + place.getId() + ", " + place.getAddress() + ", " + place.getLatLng());

                for (CF mCF : BookingCheckoutActivity.mStore.getCf()) {
                    if (getView().findViewWithTag(mCF.getLabel()) != null) {
                        ((AppCompatEditText) getView().findViewWithTag(mCF.getLabel())).setText(place.getName());
                        orderFields.put(mCF.getLabel(), place.getName() + ";" + place.getLatLng().latitude + ";" + place.getLatLng().longitude);
                    }
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the showError.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("CustomSearchFrag", status.getStatusMessage());
            } else if (resultCode == AutocompleteActivity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else if (requestCode == REQUEST_LOCATION_LAT_LNG) {
            if (resultCode == AutocompleteActivity.RESULT_OK) {
                for (CF mCF : BookingCheckoutActivity.mStore.getCf()) {
                    if (getView().findViewWithTag(mCF.getLabel()) != null) {
                        ((AppCompatEditText) getView().findViewWithTag(mCF.getLabel())).setText(data.getStringExtra("address"));
                        orderFields.put(mCF.getLabel(), data.getStringExtra("address") + ";" + data.getDoubleExtra("lat", 0) + ";" + data.getDoubleExtra("lng", 0));
                    }
                }


            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the showError.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("CustomSearchFrag", status.getStatusMessage());
            } else if (resultCode == AutocompleteActivity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}