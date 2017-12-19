package com.djavid.bitcoinrate.model.dto.heroku;

import io.realm.RealmObject;


public class Ticker extends RealmObject {

    private long id;
    private long tokenId;
    private String cryptoId;
    private String countryId;
    private double price;


    public Ticker() { }

    public Ticker(long tokenId, String cryptoId, String countryId) {
        this.tokenId = tokenId;
        this.cryptoId = cryptoId;
        this.countryId = countryId;
    }

    public Ticker(long id, long tokenId, String cryptoId, String countryId) {
        this.id = id;
        this.tokenId = tokenId;
        this.cryptoId = cryptoId;
        this.countryId = countryId;
    }


    @Override
    public String toString() {
        return "Ticker{" +
                "id=" + id +
                ", tokenId=" + tokenId +
                ", cryptoId='" + cryptoId + '\'' +
                ", countryId='" + countryId + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        Ticker ticker = (Ticker) o;

        if (id != ticker.id) return false;
        if (tokenId != ticker.tokenId) return false;
        if (Double.compare(ticker.price, price) != 0) return false;
        if (cryptoId != null ? !cryptoId.equals(ticker.cryptoId) : ticker.cryptoId != null)
            return false;
        return countryId != null ? countryId.equals(ticker.countryId) : ticker.countryId == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (tokenId ^ (tokenId >>> 32));
        result = 31 * result + (cryptoId != null ? cryptoId.hashCode() : 0);
        result = 31 * result + (countryId != null ? countryId.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public long getTokenId() {
        return tokenId;
    }
    public void setTokenId(long tokenId) {
        this.tokenId = tokenId;
    }

    public String getCryptoId() {
        return cryptoId;
    }
    public void setCryptoId(String cryptoId) {
        this.cryptoId = cryptoId;
    }

    public String getCountryId() {
        return countryId;
    }
    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}
