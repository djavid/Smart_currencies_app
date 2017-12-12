package com.djavid.bitcoinrate.model.dto.heroku;


public class Subscribe {

    private Long id;
    private boolean isTrendingUp;
    private String value;
    private long tickerId;
    private long tokenId;
    private String cryptoId;
    private String countryId;
    private double change_percent;


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

        this.value = curr_price + "";
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
