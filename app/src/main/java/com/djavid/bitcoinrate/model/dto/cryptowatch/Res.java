package com.djavid.bitcoinrate.model.dto.cryptowatch;

import java.util.List;


public class Res {

    private String symbol;
    private Integer id;
    private Base base;
    private Base quote;
    private String route;
    private List<Market> markets = null;


    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Base getBase() {
        return base;
    }
    public void setBase(Base base) {
        this.base = base;
    }

    public Base getQuote() {
        return quote;
    }
    public void setQuote(Base quote) {
        this.quote = quote;
    }

    public String getRoute() {
        return route;
    }
    public void setRoute(String route) {
        this.route = route;
    }

    public List<Market> getMarkets() {
        return markets;
    }
    public void setMarkets(List<Market> markets) {
        this.markets = markets;
    }
}
