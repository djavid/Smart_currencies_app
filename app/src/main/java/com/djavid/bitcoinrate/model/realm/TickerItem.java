package com.djavid.bitcoinrate.model.realm;

import io.realm.RealmObject;


public class TickerItem extends RealmObject {

    private double price;
    private String code;


    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
}
