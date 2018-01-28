package com.djavid.bitcoinrate.model.realm;

import com.djavid.bitcoinrate.model.heroku.Ticker;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;


public class RealmTickerList extends RealmObject {

    private RealmList<Ticker> list;


    public RealmTickerList() { }

    public RealmTickerList(List<Ticker> list) {
        this.list = new RealmList<>(list.toArray(new Ticker[list.size()]));
    }

    @Override
    public String toString() {
        return "RealmTickerList{" +
                "list=" + list +
                '}';
    }

    public RealmList<Ticker> getList() {
        return list;
    }
    public void setList(RealmList<Ticker> list) {
        this.list = list;
    }
}
