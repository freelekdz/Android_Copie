package com.freelek.apps.freelekdz.booking.controllers.parser;


import com.freelek.apps.freelekdz.booking.modals.CF;
import com.freelek.apps.freelekdz.parser.Parser;
import com.freelek.apps.freelekdz.parser.tags.Tags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmList;


public class CFParser extends Parser {

    public CFParser(JSONObject json) {
        super(json);
    }


    public RealmList<CF> getCFs() {
        RealmList<CF> list = new RealmList<CF>();

        try {

            JSONObject jsonResult = this.json.getJSONObject(Tags.RESULT);


            for (int i = 0; i < jsonResult.length(); i++) {

                JSONObject jsonRow = jsonResult.getJSONObject(String.valueOf(i));


                if (jsonRow.has("fields") && !jsonRow.isNull("fields")) {

                    JSONArray fieldsArray = new JSONArray(jsonRow.getString("fields"));
                    for (int j = 0; j < fieldsArray.length(); j++) {
                        CF mCF = new CF();
                        JSONObject field = fieldsArray.getJSONObject(j);
                        mCF.setLabel(field.getString("label"));
                        mCF.setRequired(field.getInt("required"));
                        mCF.setStep(field.getInt("step"));
                        mCF.setOrder(field.getInt("order"));
                        mCF.setType(field.getString("type"));
                        list.add(mCF);
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return list;

    }


}
