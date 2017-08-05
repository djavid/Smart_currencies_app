
package com.djavid.bitcoinrate.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CryptonatorTicker {

    @SerializedName("ticker")
    @Expose
    private Ticker ticker;
    @SerializedName("timestamp")
    @Expose
    private Long timestamp;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("error")
    @Expose
    private String error;

    /**
     * No args constructor for use in serialization
     *
     */
    public CryptonatorTicker() {
    }

    /**
     * 
     * @param timestamp
     * @param ticker
     * @param error
     * @param success
     */
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
