package com.djavid.bitcoinrate.model.heroku;

import io.realm.RealmObject;


public class Subscribe extends RealmObject {

    private Long id;
    private boolean isTrendingUp;
    private String value;
    private long tickerId;
    private long tokenId;
    private String cryptoId;
    private String countryId;
    private double change_percent;


    public Subscribe() { }

    public Subscribe(boolean isTrendingUp, String value, long tickerId, long tokenId, String cryptoId, String countryId) {

        this.isTrendingUp = isTrendingUp;
        this.value = value;
        this.tickerId = tickerId;
        this.tokenId = tokenId;
        this.cryptoId = cryptoId;
        this.countryId = countryId;

        this.change_percent = 0;
    }

    public Subscribe(String value, long tickerId, long tokenId, String cryptoId, String countryId, double curr_price) {

        this.value = Double.toString(curr_price);
        this.tickerId = tickerId;
        this.tokenId = tokenId;
        this.cryptoId = cryptoId;
        this.countryId = countryId;
        this.change_percent = Double.parseDouble(value);

        this.isTrendingUp = false;
    }

    @Override
    public String toString() {
        return "Subscribe{" +
                "id=" + id +
                ", isTrendingUp=" + isTrendingUp +
                ", value='" + value + '\'' +
                ", tickerId=" + tickerId +
                ", tokenId=" + tokenId +
                ", cryptoId='" + cryptoId + '\'' +
                ", countryId='" + countryId + '\'' +
                ", change_percent=" + change_percent +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        Subscribe subscribe = (Subscribe) o;

        if (isTrendingUp != subscribe.isTrendingUp) return false;
        if (tickerId != subscribe.tickerId) return false;
        if (tokenId != subscribe.tokenId) return false;
        if (Double.compare(subscribe.change_percent, change_percent) != 0) return false;
        if (id != null ? !id.equals(subscribe.id) : subscribe.id != null) return false;
        if (value != null ? !value.equals(subscribe.value) : subscribe.value != null) return false;
        if (cryptoId != null ? !cryptoId.equals(subscribe.cryptoId) : subscribe.cryptoId != null)
            return false;
        return countryId != null ? countryId.equals(subscribe.countryId) : subscribe.countryId == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (isTrendingUp ? 1 : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (int) (tickerId ^ (tickerId >>> 32));
        result = 31 * result + (int) (tokenId ^ (tokenId >>> 32));
        result = 31 * result + (cryptoId != null ? cryptoId.hashCode() : 0);
        result = 31 * result + (countryId != null ? countryId.hashCode() : 0);
        temp = Double.doubleToLongBits(change_percent);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public boolean isTrendingUp() {
        return isTrendingUp;
    }
    public void setTrendingUp(boolean trendingUp) {
        this.isTrendingUp = trendingUp;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public long getTickerId() {
        return tickerId;
    }
    public void setTickerId(long tickerId) {
        this.tickerId = tickerId;
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

    public long getTokenId() {
        return tokenId;
    }
    public void setTokenId(long tokenId) {
        this.tokenId = tokenId;
    }

    public double getChange_percent() {
        return change_percent;
    }
    public void setChange_percent(double change_percent) {
        this.change_percent = change_percent;
    }
}
