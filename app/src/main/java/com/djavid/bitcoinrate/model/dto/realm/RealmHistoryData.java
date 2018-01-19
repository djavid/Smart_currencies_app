package com.djavid.bitcoinrate.model.dto.realm;

import com.djavid.bitcoinrate.model.dto.cryptocompare.Datum;

import io.realm.RealmList;
import io.realm.RealmObject;


public class RealmHistoryData extends RealmObject {

    private String pair;
    private RealmList<Datum> values = null;


    public RealmHistoryData() { }

    public RealmHistoryData(RealmList<Datum> values, String pair) {
        this.pair = pair;
        this.values = values;
    }

    @Override
    public String toString() {
        return "RealmHistoryData{" +
                "values=" + values +
                '}';
    }


    public RealmList<Datum> getValues() {
        return values;
    }
    public void setValues(RealmList<Datum> values) {
        this.values = values;
    }

    public String getPair() {
        return pair;
    }
    public void setPair(String pair) {
        this.pair = pair;
    }
}
