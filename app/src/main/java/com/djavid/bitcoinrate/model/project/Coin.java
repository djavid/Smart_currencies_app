package com.djavid.bitcoinrate.model.project;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.util.Config;

import ir.mirrajabi.searchdialog.core.Searchable;


public class Coin implements Searchable {

    public String symbol;
    public String id;
    public String imageUrl;

    public Coin(String symbol, String id) {
        this.symbol = symbol;
        this.id = id;
        this.imageUrl = "";
    }

    public Coin(String symbol, String id, String imageUrl) {
        this.symbol = symbol;
        this.id = id;
        this.imageUrl = Config.IMAGE_BASE_URL + imageUrl;
    }

    @Override
    public String getTitle() {
        if (App.getAppInstance().getPreferences().getTitleFormat().equals("codes"))
            return symbol;
        else
            return id;
    }


}
