package com.freelek.apps.freelekdz.booking.controllers.helpers;

public class generateViews {

   /* @SuppressLint({"ResourceType", "ClickableViewAccessibility"})
    private void parserInputViews(View view) {


        LinearLayout itemWrapper = view.findViewById(R.id.item_wrapper);

        if (BookingCheckoutActivity.mStore != null) {

            int userId = SessionsController.getSession().getUser().getId();
            int cfId = BookingCheckoutActivity.mStore.getCf_id();
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

            for (CF mCF : BookingCheckoutActivity.mStore.getCf()) {
                if (mCF.getType() != null) {
                    //List<String> arrayType = Arrays.asList(mCF.getType().split("."));
                    String[] arrayType = mCF.getType().split("\\.");
                    if (arrayType.length > 0 && (arrayType[0].equals("input") || arrayType[0].equals("textarea"))) {

                        if (arrayType[1].equals("number")) {
                            //generate incremental view with two button
                            generateIncrementerLayout(mCF, itemWrapper, getContext());

                        } else {

                            TextInputLayout txtInpLayout = new TextInputLayout(view.getContext());
                            txtInpLayout.setHintTextAppearance(R.style.cf_et_style);

                            LinearLayout.LayoutParams TILlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            txtInpLayout.setLayoutParams(TILlp);

                            AppCompatEditText et = new AppCompatEditText(view.getContext());
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


                            //set data if exist
                            if (orderFields.containsKey(mCF.getLabel()) && orderFields.get(mCF.getLabel()) != null) {
                                et.setText(orderFields.get(mCF.getLabel()));
                                et.setVisibility(View.GONE);
                            } else {
                                orderFields.put(mCF.getLabel(), "");

                            }

                            switch (arrayType[1]) {
                                case "text":
                                    et.setInputType(InputType.TYPE_CLASS_TEXT);
                                    break;
                                case "phone":
                                    et.setInputType(InputType.TYPE_CLASS_PHONE);
                                    break;
                                case "date":

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

                                    break;
                                case "time":

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

                                    break;
                                case "location":

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

                                    break;

                            }


                            itemWrapper.addView(txtInpLayout);
                        }


                    }
                }
            }

        }


    }*/

}
