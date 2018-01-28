package com.djavid.bitcoinrate.model.cryptonator;


public class CryptonatorTicker {

    private Ticker ticker;
    private Long timestamp;
    private Boolean success;
    private String error;


    public CryptonatorTicker() { }

    public CryptonatorTicker(Ticker ticker, Long timestamp, Boolean success, String error) {
        super();
        this.ticker = ticker;
        this.timestamp = timestamp;
        this.success = success;
        this.error = error;
    }

    public Ticker getTicker() {
        return ticker;
    }
    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    public Long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getSuccess() {
        return success;
    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }

}
