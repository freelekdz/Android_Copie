package com.freelek.apps.freelekdz.booking.modals;

import com.freelek.apps.freelekdz.classes.Currency;
import com.freelek.apps.freelekdz.classes.Images;

import io.realm.RealmList;

public interface OrderableItem {

    String getShort_description();

    String getLink();

    int getFeatured();

    Double getLat();

    Double getLng();

    Double getDistance();

    String getStore_name();

    Images getImages();

    String getName();

    int getId();

    void setId(int id);

    Currency getCurrency();

    int getStore_id();

    String getDate_start();

    String getDate_end();

    int getStatus();

    int getUser_id();

    String getDescription();

    String getProduct_type();

    float getProduct_value();

    String getTags();

    int getIs_deal();

    int getOrder_enabled();

    String getOrder_button();

    int getCf_id();

    RealmList<CF> getCf();

    RealmList<Images> getListImages();

    int getQty_enabled();

    int getCommission();

    int getStock();

    int getIs_offer();

    RealmList<Service> getVariants();

}
