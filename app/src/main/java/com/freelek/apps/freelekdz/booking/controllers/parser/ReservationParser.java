package com.freelek.apps.freelekdz.booking.controllers.parser;

import com.freelek.apps.freelekdz.booking.modals.Reservation;
import com.freelek.apps.freelekdz.parser.Parser;
import com.freelek.apps.freelekdz.parser.tags.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmList;


public class ReservationParser extends Parser {

    public ReservationParser(JSONObject json) {
        super(json);
    }

    public RealmList<Reservation> getOrders() {

        RealmList<Reservation> list = new RealmList<>();

        try {

            JSONObject json_array = json.getJSONObject(Tags.RESULT);

            for (int i = 0; i < json_array.length(); i++) {


                try {
                    JSONObject json_order = json_array.getJSONObject(i + "");
                    Reservation reservation = new Reservation();
                    reservation.setId(json_order.getInt("id"));
                    reservation.setStatus(json_order.getString("status"));
                    reservation.setName(json_order.getString("name"));
                    reservation.setId_store(json_order.getInt("id_store"));
                    reservation.setUser_id(json_order.getInt("user_id"));
                    reservation.setReq_cf_id(json_order.getInt("cf_id"));
                    reservation.setCart(json_order.getString("cart"));
                    reservation.setReq_cf_data(json_order.getString("cf_data"));
                    reservation.setUpdated_at(json_order.getString("updated_at"));
                    reservation.setCreated_at(json_order.getString("created_at"));


                    if (json_order.has("payment_status") && !json_order.isNull("payment_status"))
                        reservation.setPayment_status(json_order.getString("payment_status"));


                    if (json_order.has("amount") && !json_order.isNull("amount"))
                        reservation.setAmount(json_order.getDouble("amount"));


                    if (!json_order.isNull("items")) {
                        ItemParser items = new ItemParser(json_order, json_order.getInt("id"));
                        reservation.setItems(items.getItems());
                    } else {
                        reservation.setItems(null);
                    }

                    list.add(reservation);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        } catch (
                JSONException e) {
            e.printStackTrace();
        }


        return list;
    }


}
