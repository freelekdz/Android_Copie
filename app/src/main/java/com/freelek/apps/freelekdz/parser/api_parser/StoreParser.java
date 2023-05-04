package com.freelek.apps.freelekdz.parser.api_parser;


import android.util.Log;

import com.freelek.apps.freelekdz.appconfig.AppContext;
import com.freelek.apps.freelekdz.booking.controllers.parser.CFParser;
import com.freelek.apps.freelekdz.booking.controllers.parser.ServiceParser;
import com.freelek.apps.freelekdz.classes.Images;
import com.freelek.apps.freelekdz.classes.Store;
import com.freelek.apps.freelekdz.classes.User;
import com.freelek.apps.freelekdz.parser.Parser;
import com.freelek.apps.freelekdz.parser.tags.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmList;


public class StoreParser extends Parser {

    public StoreParser(JSONObject json) {
        super(json);
    }

    public RealmList<Store> getStore() {

        RealmList<Store> list = new RealmList<Store>();

        try {

            JSONObject json_array = json.getJSONObject(Tags.RESULT);

            for (int i = 0; i < json_array.length(); i++) {


                try {
                    JSONObject json_user = json_array.getJSONObject(i + "");
                    Store store = new Store();
                    store.setId(json_user.getInt("id_store"));
                    store.setName(json_user.getString("name"));
                    store.setAddress(json_user.getString("address"));
                    store.setLatitude(json_user.getDouble("latitude"));
                    store.setLongitude(json_user.getDouble("longitude"));
                    store.setCategory_id(json_user.getInt("category_id"));
                    store.setStatus(json_user.getInt("status"));
                    store.setGallery(json_user.getInt("gallery"));
                    store.setCategory_name(json_user.getString("category_name"));
                    store.setVoted(json_user.getBoolean("voted"));
                    store.setVotes((float) json_user.getDouble("votes"));
                    store.setNbr_votes(json_user.getString("nbr_votes"));
                    store.setNbrOffers(json_user.getInt("nbrOffers"));


                    /********* begin : booking update **********/

                    if (json_user.has("nbrServices"))
                        store.setNbrServices(json_user.getInt("nbrServices"));

                    if (json_user.has("services")) {
                        JSONObject services = new JSONObject(json_user.getString("services"));
                        ServiceParser pp = new ServiceParser(services);
                        store.setServices(pp.getVariants());
                    }

                    if (json_user.has("cf_id") && !json_user.isNull("cf_id"))
                        store.setCf_id(json_user.getInt("cf_id"));

                    if (json_user.has("cf") && !json_user.isNull("cf")) {
                        CFParser mProductCurrencyParser = new CFParser(new JSONObject(json_user.getString("cf")));
                        store.setCf(mProductCurrencyParser.getCFs());
                    }

                    if (json_user.has("book") && !json_user.isNull("book"))
                        store.setBook(json_user.getInt("book"));


                    /********* end : booking update **********/


                    if (json_user.has("website"))
                        store.setWebsite(json_user.getString("website"));

                    if (json_user.has("category_color") && !json_user.isNull("category_color"))
                        store.setCategory_color(json_user.getString("category_color"));

                    if (json_user.has("canChat") && !json_user.isNull("canChat"))
                        store.setCanChat(json_user.getInt("canChat"));

                    if (json_user.has("link"))
                        store.setLink(json_user.getString("link"));


                    try {
                        store.setDistance(json_user.getDouble("distance"));
                    } catch (Exception e) {
                        store.setDistance(0.0);
                    }


                    if (json_user.has("video_url"))
                        store.setVideo_url(json_user.getString("video_url"));


                    if (json_user.has("telephone"))
                        store.setPhone(json_user.getString("telephone"));


                    if (json_user.has("saved") && !json_user.isNull("saved"))
                        store.setSaved(json_user.getInt("saved"));


                    try {
                        store.setOpening(json_user.getInt("opening"));
                    } catch (Exception e) {
                        store.setOpening(0);
                    }

                    try {

                        store.setOpening_time_table(json_user.getString("opening_time_table"));
                        JSONObject opt = new JSONObject(json_user.getString("opening_time_table"));

                        OpeningTimeTableParser optp = new OpeningTimeTableParser(opt);
                        store.setOpening_time_table_list(optp.getList());

                    } catch (Exception e) {
                        e.printStackTrace();
                        store.setOpening_time_table("");
                    }

                    try {
                        store.setLastOffer(json_user.getString("lastOffer"));
                    } catch (Exception e) {
                        store.setLastOffer("");
                    }


                    if (json_user.has("user_id") && !json_user.isNull("user_id"))
                        store.setUser_id(json_user.getInt("user_id"));


                    if (json_user.has("featured") && !json_user.isNull("featured"))
                        store.setFeatured(json_user.getInt("featured"));


                    try {
                        store.setDescription(json_user.getString("description"));
                    } catch (Exception e) {
                        store.setDescription("");
                    }

                    try {
                        store.setDetail(json_user.getString("detail"));
                    } catch (Exception e) {
                        store.setDescription("");
                    }



                    JSONObject json_user_manger = new JSONObject(json_user.getString("user"));
                    UserParser mUserParserSender = new UserParser(json_user_manger);
                    User manager = mUserParserSender.getUser().get(0);

                    if (manager != null) {
                        store.setUser(manager);

                        if (AppContext.DEBUG)
                            Log.e("StoreParserManager", manager.getUsername() + "- " + manager.getId() + " sss " + store.getCategory_id());

                    }
                    String jsonValues = "";
                    try {

                        if (!json_user.isNull("images")) {
                            jsonValues = json_user.getJSONObject("images").toString();
                            JSONObject jsonObject = new JSONObject(jsonValues);
                            ImagesParser imgp = new ImagesParser(jsonObject);

                            if (imgp.getImagesList().size() > 0) {
                                store.setImages(imgp.getImagesList().get(0));
                                store.setListImages(imgp.getImagesList());
                                store.setImageJson(jsonObject.toString());
                            }


                        }

                    } catch (JSONException jex) {
                        store.setListImages(new RealmList<Images>());
                    }

                    list.add(store);
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
