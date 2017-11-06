package com.djavid.bitcoinrate.model.dto;


public class CoinMarketCapTicker {

    private String id;
    private String name;
    private String symbol;
    private int rank;
    private double market_cap_usd;
    private double available_supply;
    private double total_supply;
    private double percent_change_1h;
    private double percent_change_24h;
    private double percent_change_7d;
    private long last_updated;

    private double price_btc;
    private double price_usd;
    private double price_eur;
    private double price_cad;
    private double price_cny;
    private double price_jpy;
    private double price_pln;
    private double price_gbp;
    private double price_rub;
    private double price_uah;


    public Double getPrice(String id) {
        switch (id) {
            case "USD":
                return getPrice_usd();
            case "EUR":
                return getPrice_eur();

            case "CAD":
                return getPrice_cad();
            case "CNY":
                return getPrice_cny();
            case "JPY":
                return getPrice_jpy();
            case "PLN":
                return getPrice_pln();
            case "GBP":
                return getPrice_gbp();
            case "RUB":
                return getPrice_rub();
            case "UAH":
                return getPrice_uah();
            default:
                return 0d;
        }
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getPrice_btc() {
        return price_btc;
    }
    public void setPrice_btc(double price_btc) {
        this.price_btc = price_btc;
    }

    public double getMarket_cap_usd() {
        return market_cap_usd;
    }
    public void setMarket_cap_usd(double market_cap_usd) {
        this.market_cap_usd = market_cap_usd;
    }

    public double getAvailable_supply() {
        return available_supply;
    }
    public void setAvailable_supply(double available_supply) {
        this.available_supply = available_supply;
    }

    public double getTotal_supply() {
        return total_supply;
    }
    public void setTotal_supply(double total_supply) {
        this.total_supply = total_supply;
    }

    public double getPercent_change_1h() {
        return percent_change_1h;
    }
    public void setPercent_change_1h(double percent_change_1h) {
        this.percent_change_1h = percent_change_1h;
    }

    public double getPercent_change_24h() {
        return percent_change_24h;
    }
    public void setPercent_change_24h(double percent_change_24h) {
        this.percent_change_24h = percent_change_24h;
    }

    public double getPercent_change_7d() {
        return percent_change_7d;
    }
    public void setPercent_change_7d(double percent_change_7d) {
        this.percent_change_7d = percent_change_7d;
    }

    public long getLast_updated() {
        return last_updated;
    }
    public void setLast_updated(long last_updated) {
        this.last_updated = last_updated;
    }


    public double getPrice_usd() {
        return price_usd;
    }
    public void setPrice_usd(double price_usd) {
        this.price_usd = price_usd;
    }

    public double getPrice_eur() {
        return price_eur;
    }
    public void setPrice_eur(double price_eur) {
        this.price_eur = price_eur;
    }

    public double getPrice_cad() {
        return price_cad;
    }
    public void setPrice_cad(double price_cad) {
        this.price_cad = price_cad;
    }

    public double getPrice_cny() {
        return price_cny;
    }
    public void setPrice_cny(double price_cny) {
        this.price_cny = price_cny;
    }

    public double getPrice_jpy() {
        return price_jpy;
    }
    public void setPrice_jpy(double price_jpy) {
        this.price_jpy = price_jpy;
    }

    public double getPrice_pln() {
        return price_pln;
    }
    public void setPrice_pln(double price_pln) {
        this.price_pln = price_pln;
    }

    public double getPrice_gbp() {
        return price_gbp;
    }
    public void setPrice_gbp(double price_gbp) {
        this.price_gbp = price_gbp;
    }

    public double getPrice_rub() {
        return price_rub;
    }
    public void setPrice_rub(double price_rub) {
        this.price_rub = price_rub;
    }

    public double getPrice_uah() {
        return price_uah;
    }
    public void setPrice_uah(double price_uah) {
        this.price_uah = price_uah;
    }
}