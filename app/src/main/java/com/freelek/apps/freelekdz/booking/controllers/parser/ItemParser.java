package com.freelek.apps.freelekdz.booking.controllers.parser;

import com.freelek.apps.freelekdz.booking.modals.Item;
import com.freelek.apps.freelekdz.parser.Parser;
import com.freelek.apps.freelekdz.parser.api_parser.OfferCurrencyParser;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmList;


public class ItemParser extends Parser {

    int booking_id;

    public ItemParser(JSONObject json, int _order_id) {
        super(json);

        booking_id = _order_id;
    }

    public RealmList<Item> getItems() {

        RealmList<Item> list = new RealmList<>();

        try {

            JSONObject json_array = json.getJSONObject("items");


            for (int i = 0; i < json_array.length(); i++) {

                try {
                    JSONObject json_user = json_array.getJSONObject(i + "");
                    Item item = new Item();
                    item.setId(booking_id + "_" + json_user.getInt("id"));
                    item.setName(json_user.getString("name"));
                    item.setModule(json_user.getString("module"));
                    item.setAmount(json_user.getDouble("amount"));

                    if (json_user.has("image"))
                        item.setImage(json_user.getString("image"));

                    if (json_user.has("qty"))
                        item.setQty(json_user.getInt("qty"));

                    if (json_user.has("services"))
                        item.setService(json_user.getString("services"));


                    if (json_user.has("options"))
                        item.setOptions(json_user.getString("options"));

                    if (json_user.has("currency") && !json_user.isNull("currency")) {
                        OfferCurrencyParser mProductCurrencyParser = new OfferCurrencyParser(new JSONObject(
                                json_user.getString("currency")
                        ));
                        item.setCurrency(mProductCurrencyParser.getCurrency());

                    }

                    list.add(item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
    }


}
