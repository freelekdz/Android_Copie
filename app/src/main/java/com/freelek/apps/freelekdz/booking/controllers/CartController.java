package com.freelek.apps.freelekdz.booking.controllers;


import com.freelek.apps.freelekdz.Services.NotifyDataNotificationEvent;
import com.freelek.apps.freelekdz.appconfig.Constances;
import com.freelek.apps.freelekdz.booking.modals.Cart;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class CartController {

    public static Cart findCartById(int id) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Cart.class).equalTo("id", id).findFirst();
    }

    public static Cart findItemByModule(final String module, final int module_id) {

        Realm realm = Realm.getDefaultInstance();
        Cart obj = realm.where(Cart.class)
                .equalTo("module", module)
                .equalTo("module_id", module_id)
                .findFirst();

        return obj;
    }


    public static boolean addServiceToCart(final Cart item) {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {


                //generate auto increment field
                Number currentIdNum = realm.where(Cart.class).max("id");
                int nextId;
                if (currentIdNum == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNum.intValue() + 1;
                }
                item.setId(nextId);
                realm.copyToRealmOrUpdate(item);

                //notify using event bus
                EventBus.getDefault().postSticky(new NotifyDataNotificationEvent("cart_badge_counter"));
            }
        });
        return true;
    }


    public static void removeAll() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Cart> result = realm.where(Cart.class).findAll();
                result.deleteAllFromRealm();

                //notify using event bus
                EventBus.getDefault().postSticky(new NotifyDataNotificationEvent("cart_badge_counter"));
            }
        });

    }

    public static boolean removeItem(int id) {
        Realm realm = Realm.getDefaultInstance();
        final boolean[] ret = {true};
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Cart cart = realm.where(Cart.class).equalTo("id", id).findFirst();
                if (cart != null) {
                    cart.deleteFromRealm();
                } else {
                    ret[0] = false;
                }

                //notify using event bus
                EventBus.getDefault().postSticky(new NotifyDataNotificationEvent("cart_badge_counter"));
            }
        });
        return ret[0];

    }


    public static int productCartCounter(final int user_id) {
        return Realm.getDefaultInstance().where(Cart.class)
                .isNotNull("product")
                .equalTo("module", Constances.ModulesConfig.SERVICE_MODULE)
                .equalTo("user_id", user_id)
                .findAll().size();
    }


    public static RealmList<Cart> listProducts(final int user_id) {

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Cart> result = realm.where(Cart.class)
                .isNotNull("product")
                .equalTo("module", Constances.ModulesConfig.SERVICE_MODULE)
                .equalTo("user_id", user_id)
                .findAll();

        RealmList<Cart> results = new RealmList<Cart>();
        results.addAll(result.subList(0, result.size()));

        return results;
    }


    public static boolean checkProductStore(final int parent_id, final int user_id) {
        final boolean[] ret = {true};
        List<Cart> listPorducts = listProducts(user_id);
        if (listPorducts.size() > 0)
            for (Cart cart : listPorducts) {
                if (cart.getParent_id() != parent_id) {
                    ret[0] = false;
                    break;
                }
            }
        return ret[0];
    }


}
