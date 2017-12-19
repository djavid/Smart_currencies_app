package com.djavid.bitcoinrate.presenter.instancestate;


public class RateFragmentInstanceState {

    private String timespan;
    private String price;


    public RateFragmentInstanceState(String timespan, String price) {
        this.timespan = timespan;
        this.price = price;
    }

    public String getTimespan() {
        return timespan;
    }
    public void setTimespan(String timespan) {
        this.timespan = timespan;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
}
