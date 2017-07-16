
package com.djavid.bitcoinrate.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Market {

    @SerializedName("market")
    @Expose
    private String market;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("volume")
    @Expose
    private Double volume;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Market() {
    }

    /**
     * 
     * @param price
     * @param market
     * @param volume
     */
    public Market(String market, Double price, Double volume) {
        super();
        this.market = market;
        this.price = price;
        this.volume = volume;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

}
