package com.djavid.bitcoinrate.model.dto.realm;

import io.realm.RealmList;
import io.realm.RealmObject;


public class RealmDoubleList extends RealmObject {

    private RealmList<Double> list;


    public RealmDoubleList() { }

    public RealmDoubleList(Double[] list) {
        this.list = new RealmList<>(list);
    }

    @Override
    public String toString() {
        return "RealmDoubleList{" +
                "list=" + list +
                '}';
    }

    public RealmList<Double> getList() {
        return list;
    }
    public void setList(RealmList<Double> list) {
        this.list = list;
    }
}
