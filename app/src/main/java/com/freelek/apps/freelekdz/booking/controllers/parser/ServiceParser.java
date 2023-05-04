package com.freelek.apps.freelekdz.booking.controllers.parser;

import com.freelek.apps.freelekdz.booking.modals.Service;
import com.freelek.apps.freelekdz.parser.Parser;
import com.freelek.apps.freelekdz.parser.api_parser.OfferCurrencyParser;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmList;

public class ServiceParser extends Parser {


    public ServiceParser(JSONObject json) {
        super(json);
    }


    public RealmList<Service> getVariants() {

        RealmList<Service> list = new RealmList<>();

        try {

            for (int i = 0; i < json.length(); i++) {

                JSONObject json_options = json.getJSONObject(i + "");
                Service service = new Service();

                service.setGroup_id(json_options.getInt("group_id"));
                service.setGroup_label(json_options.getString("group_label"));
                service.setType(json_options.getString("type"));

                if (json_options.has("options") && !json_options.isNull("options")) {
                    OptionParser optionsParser = new OptionParser(new JSONObject(json_options.getString("options")));
                    service.setOptions(optionsParser.getOptions());
                }

                if (json_options.has("currency") && !json_options.isNull("currency")) {
                    OfferCurrencyParser mProductCurrencyParser = new OfferCurrencyParser(new JSONObject(
                            json_options.getString("currency")
                    ));
                    service.setCurrency(mProductCurrencyParser.getCurrency());
                }

                list.add(service);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
    }
}
