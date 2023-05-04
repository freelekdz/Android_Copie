package com.freelek.apps.freelekdz.booking.modals;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Reservation extends RealmObject {


    @Ignore
    public boolean expanded = false;
    @Ignore
    public boolean parent = false;
    @Ignore
    public boolean swiped = false; // flag when item swiped
    @PrimaryKey
    private int id;
    private int user_id;
    private int id_store;
    private String name;
    private int req_cf_id;
    private int status_id;
    private String status;
    private String module;
    private String cart;
    private String req_cf_data;
    private String updated_at;
    private String created_at;
    private RealmList<Item> items;
    private double amount;
    private RealmList<Service> services;
    private String extras;
    private String payment_status_data;
    private String payment_status;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCart() {
        return cart;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    public String getReq_cf_data() {
        return req_cf_data;
    }

    public void setReq_cf_data(String req_cf_data) {
        this.req_cf_data = req_cf_data;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public RealmList<Item> getItems() {
        return items;
    }

    public void setItems(RealmList<Item> items) {
        this.items = items;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public int getId_store() {
        return id_store;
    }

    public void setId_store(int id_store) {
        this.id_store = id_store;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReq_cf_id() {
        return req_cf_id;
    }

    public void setReq_cf_id(int req_cf_id) {
        this.req_cf_id = req_cf_id;
    }


    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public RealmList<Service> getServices() {
        return services;
    }

    public void setServices(RealmList<Service> services) {
        this.services = services;
    }


    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }


    public String getPayment_status_data() {
        return payment_status_data;
    }

    public void setPayment_status_data(String payment_status_data) {
        this.payment_status_data = payment_status_data;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }
}
