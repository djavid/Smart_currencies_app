package com.djavid.bitcoinrate.model.dto.cryptonator;

import java.util.List;


public class Ticker {

    private String base;
    private String target;
    private double price;
    private String volume;
    private double change;
    private List<Market> markets = null;


    public Ticker() { }

    public Ticker(String base, String target, double price, String volume, double change, List<Market> markets) {
        super();
        this.base = base;
        this.target = target;
        this.price = price;
        this.volume = volume;
        this.change = change;
        this.markets = markets;
    }

    public String getBase() {
        return base;
    }
    public void setBase(String base) {
        this.base = base;
    }

    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public String getVolume() {
        return volume;
    }
    public void setVolume(String volume) {
        this.volume = volume;
    }

    public double getChange() {
        return change;
    }
    public void setChange(double change) {
        this.change = change;
    }

    public List<Market> getMarkets() {
        return markets;
    }
    public void setMarkets(List<Market> markets) {
        this.markets = markets;
    }

}
