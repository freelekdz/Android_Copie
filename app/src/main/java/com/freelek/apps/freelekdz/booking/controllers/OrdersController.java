package com.freelek.apps.freelekdz.booking.controllers;


import com.freelek.apps.freelekdz.booking.modals.Reservation;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Droideve on 11/12/2017.
 */

public class OrdersController {


    public static Reservation findOrderById(int id) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Reservation.class).equalTo("id", id).findFirst();
    }


    public static boolean insertOrders(final RealmList<Reservation> list) {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Reservation reservation : list) {
                    realm.copyToRealmOrUpdate(reservation);
                }
            }
        });
        return true;
    }


    public static void removeAll() {
        Realm realm = Realm.getDefaultInstance();
        if (realm.isInTransaction()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<Reservation> result = realm.where(Reservation.class).findAll();
                    result.deleteAllFromRealm();
                }
            });
        }

    }

}
