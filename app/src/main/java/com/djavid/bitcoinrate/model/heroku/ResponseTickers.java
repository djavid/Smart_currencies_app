package com.djavid.bitcoinrate.model.heroku;

import java.util.List;


public class ResponseTickers {

    public String error;
    public List<Ticker> tickers;


    public ResponseTickers(String error) {
        this.error = error;
    }

    public ResponseTickers(String error, List<Ticker> tickers) {
        this.error = error;
        this.tickers = tickers;
    }

    public ResponseTickers(List<Ticker> tickers) {
        this.tickers = tickers;
        error = "";
    }

}
