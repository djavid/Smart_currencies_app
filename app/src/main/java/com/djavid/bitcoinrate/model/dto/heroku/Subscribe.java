package com.djavid.bitcoinrate.model.dto.heroku;


public class Subscribe {

    private String currId;
    private String countryId;
    private boolean isTrendingUp;
    private String value;
    private long tokenId;

    public Subscribe(String currId, String countryId, boolean isTrendingUp, String value, long tokenId) {
        this.currId = currId;
        this.countryId = countryId;
        this.isTrendingUp = isTrendingUp;
        this.value = value;
        this.tokenId = tokenId;
    }

    @Override
    public String toString() {
        return "Subscribe{" +
                "currId='" + currId + '\'' +
                ", countryId='" + countryId + '\'' +
                ", isTrendingUp=" + isTrendingUp +
                ", value='" + value + '\'' +
                ", tokenId=" + tokenId +
                '}';
    }


    public String getCurrId() {
        return currId;
    }
    public void setCurrId(String currId) {
        this.currId = currId;
    }

    public String getCountryId() {
        return countryId;
    }
    public void setCountryId(String countryId) {
        this.countryId = countryId;
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

    public long getTokenId() {
        return tokenId;
    }
    public void setTokenId(long tokenId) {
        this.tokenId = tokenId;
    }
}
