package com.freelek.apps.freelekdz.booking.modals;

import com.freelek.apps.freelekdz.classes.Offer;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Cart extends RealmObject {

    @PrimaryKey
    private int id;
    private String module;
    private int module_id;
    private double amount;
    private RealmList<Service> selectedService;
    private Offer offer;
    private RealmList<Service> services;
    private int qte = 1;
    private int status;
    private int user_id;
    private int parent_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }

    public RealmList<Service> getSelectedService() {
        return selectedService;
    }

    public void setSelectedService(RealmList<Service> selectedService) {
        this.selectedService = selectedService;
    }

    public RealmList<Service> getServices() {
        return selectedService;
    }

    public void setServices(RealmList<Service> services) {
        this.selectedService = services;
    }
}
