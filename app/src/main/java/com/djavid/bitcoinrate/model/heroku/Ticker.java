package com.djavid.bitcoinrate.model.heroku;

import io.realm.RealmObject;


public class Ticker extends RealmObject {

    private long id;
    private long tokenId;
    private String cryptoId;
    private String countryId;
    private CurrencyUpdate ticker;


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
                ", ticker=" + ticker +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        Ticker ticker1 = (Ticker) o;

        if (id != ticker1.id) return false;
        if (tokenId != ticker1.tokenId) return false;
        if (cryptoId != null ? !cryptoId.equals(ticker1.cryptoId) : ticker1.cryptoId != null)
            return false;
        if (countryId != null ? !countryId.equals(ticker1.countryId) : ticker1.countryId != null)
            return false;
        return ticker != null ? ticker.equals(ticker1.ticker) : ticker1.ticker == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (tokenId ^ (tokenId >>> 32));
        result = 31 * result + (cryptoId != null ? cryptoId.hashCode() : 0);
        result = 31 * result + (countryId != null ? countryId.hashCode() : 0);
        result = 31 * result + (ticker != null ? ticker.hashCode() : 0);
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

    public CurrencyUpdate getTicker() {
        return ticker;
    }
    public void setTicker(CurrencyUpdate ticker) {
        this.ticker = ticker;
    }

}
