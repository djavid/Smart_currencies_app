package com.djavid.bitcoinrate.model.dto.realm;

import io.realm.RealmList;
import io.realm.RealmObject;


public class RealmHistoryData extends RealmObject {

    private String pair;
    private RealmList<RealmDoubleList> values = null;


    public RealmHistoryData() { }

    public RealmHistoryData(RealmList<RealmDoubleList> values, String pair) {
        this.pair = pair;
        this.values = values;
    }

    @Override
    public String toString() {
        return "RealmHistoryData{" +
                "values=" + values +
                '}';
    }

    public RealmList<RealmDoubleList> getValues() {
        return values;
    }
    public void setValues(RealmList<RealmDoubleList> values) {
        this.values = values;
    }

    public String getPair() {
        return pair;
    }
    public void setPair(String pair) {
        this.pair = pair;
    }
}
