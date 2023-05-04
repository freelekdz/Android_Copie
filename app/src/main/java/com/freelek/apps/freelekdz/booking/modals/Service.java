package com.freelek.apps.freelekdz.booking.modals;


import com.freelek.apps.freelekdz.classes.Currency;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Service extends RealmObject{

    @Ignore
    public static String ONE_OPTION = "one_option";
    @Ignore
    public static String MULTI_OPTIONS = "multi_options";

    @PrimaryKey
    private int group_id;
    private String group_label;
    private String type;
    private RealmList<Option> options;
    private Currency currency;


    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getGroup_label() {
        return group_label;
    }

    public void setGroup_label(String group_label) {
        this.group_label = group_label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RealmList<Option> getOptions() {
        return options;
    }

    public void setOptions(RealmList<Option> options) {
        this.options = options;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }


}
