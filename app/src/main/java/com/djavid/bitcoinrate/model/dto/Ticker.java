
package com.djavid.bitcoinrate.model.dto;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ticker {

    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("target")
    @Expose
    private String target;
    @SerializedName("price")
    @Expose
    private double price;
    @SerializedName("volume")
    @Expose
    private String volume;
    @SerializedName("change")
    @Expose
    private double change;
    @SerializedName("markets")
    @Expose
    private List<Market> markets = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Ticker() {
    }

    /**
     * 
     * @param price
     * @param markets
     * @param change
     * @param volume
     * @param target
     * @param base
     */
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
