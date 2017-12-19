package com.djavid.bitcoinrate.model.dto.cryptowatch;


public class Base {

    private Integer id;
    private String symbol;
    private String name;
    private Boolean fiat;
    private String route;


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Boolean getFiat() {
        return fiat;
    }
    public void setFiat(Boolean fiat) {
        this.fiat = fiat;
    }

    public String getRoute() {
        return route;
    }
    public void setRoute(String route) {
        this.route = route;
    }

}
