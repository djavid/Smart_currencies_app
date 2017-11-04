package com.djavid.bitcoinrate.model.realm;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by djavid on 04.11.17.
 */


public class RealmCRUD {

    private Realm realm;


    public RealmCRUD() {
        realm = Realm.getDefaultInstance();
    }

    public void close() {
        realm.close();
    }

    public void createTicker(String code_crypto, String code_country) {
        realm.executeTransaction(realm -> {
            TickerItemRealm tickerItemRealm = realm.createObject(TickerItemRealm.class);
            tickerItemRealm.setCode_crypto(code_crypto);
            tickerItemRealm.setCode_country(code_country);
        });
    }

    public RealmResults getAllTickers() {
        return realm.where(TickerItemRealm.class).findAll();
    }


}
