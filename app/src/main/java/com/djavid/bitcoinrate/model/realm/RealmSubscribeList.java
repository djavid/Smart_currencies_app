package com.djavid.bitcoinrate.model.realm;

import com.djavid.bitcoinrate.model.heroku.Subscribe;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;


public class RealmSubscribeList extends RealmObject {

    private RealmList<Subscribe> list;


    public RealmSubscribeList() { }

    public RealmSubscribeList(List<Subscribe> list) {
        this.list = new RealmList<>(list.toArray(new Subscribe[list.size()]));
    }

    @Override
    public String toString() {
        return "RealmSubscribeList{" +
                "list=" + list +
                '}';
    }

    public RealmList<Subscribe> getList() {
        return list;
    }
    public void setList(RealmList<Subscribe> list) {
        this.list = list;
    }
}
