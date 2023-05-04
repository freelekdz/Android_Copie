package com.freelek.apps.freelekdz.booking.controllers;

import com.freelek.apps.freelekdz.booking.modals.Cart;

import io.realm.Realm;
import io.realm.RealmResults;


public class ProductsController {


    public static Cart findServiceByStoreId(int store_id) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Cart.class).equalTo("module_id", store_id).findFirst();
    }


    public static void removeAll() {
        Realm realm = Realm.getDefaultInstance();
        if (realm.isInTransaction()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<Cart> result = realm.where(Cart.class).findAll();
                    result.deleteAllFromRealm();

                }
            });
        }

    }

}
