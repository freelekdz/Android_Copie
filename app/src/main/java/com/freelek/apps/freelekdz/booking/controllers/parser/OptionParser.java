package com.freelek.apps.freelekdz.booking.controllers.parser;


import com.freelek.apps.freelekdz.booking.modals.Option;
import com.freelek.apps.freelekdz.parser.Parser;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmList;

public class OptionParser extends Parser {


    public OptionParser(JSONObject json) {
        super(json);
    }


    public RealmList<Option> getOptions() {

        RealmList<Option> list = new RealmList<Option>();

        try {
            
            for (int i = 0; i < json.length(); i++) {


                JSONObject json_options = json.getJSONObject(i + "");
                Option option = new Option();

                option.setId(json_options.getInt("id"));
                option.setStore_id(json_options.getInt("store_id"));
                option.setParent_id(json_options.getInt("parent_id"));
                option.setLabel(json_options.getString("label"));

                if(json_options.has("description") && !json_options.isNull("description"))
                option.setDescription(json_options.getString("description"));

                option.setValue(json_options.getDouble("value"));
                option.setOption_type(json_options.getString("option_type"));
                option.set_order(json_options.getInt("_order"));
                option.setCreated_at(json_options.getString("created_at"));
                option.setUpdated_at(json_options.getString("updated_at"));
                option.setParsed_value(json_options.getString("parsed_value"));
                option.setHidden(json_options.getInt("hidden"));

                list.add(option);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
    }
}
