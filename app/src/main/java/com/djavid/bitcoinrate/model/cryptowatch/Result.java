package com.djavid.bitcoinrate.model.cryptowatch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Result {

    @SerializedName("900")
    @Expose
    private List<List<Double>> values900 = null;

    @SerializedName("3600")
    @Expose
    private List<List<Double>> values3600 = null;

    @SerializedName("7200")
    @Expose
    private List<List<Double>> values7200 = null;

    @SerializedName("21600")
    @Expose
    private List<List<Double>> values21600 = null;

    @SerializedName("86400")
    @Expose
    private List<List<Double>> values86400 = null;

    @SerializedName("259200")
    @Expose
    private List<List<Double>> values259200 = null;


    public List<List<Double>> getValues() {

        if (getValues900() != null) return getValues900();
        if (getValues3600() != null) return getValues3600();
        if (getValues7200() != null) return getValues7200();
        if (getValues21600() != null) return getValues21600();
        if (getValues86400() != null) return getValues86400();
        if (getValues259200() != null) return getValues259200();

        return getValues86400();
    }


    private List<List<Double>> getValues86400() {
        return values86400;
    }
    private void setValues86400(List<List<Double>> values86400) {
        this.values86400 = values86400;
    }

    private List<List<Double>> getValues7200() {
        return values7200;
    }
    private void setValues7200(List<List<Double>> values7200) {
        this.values7200 = values7200;
    }

    public List<List<Double>> getValues3600() {
        return values3600;
    }
    public void setValues3600(List<List<Double>> values3600) {
        this.values3600 = values3600;
    }

    public List<List<Double>> getValues900() {
        return values900;
    }
    public void setValues900(List<List<Double>> values900) {
        this.values900 = values900;
    }

    public List<List<Double>> getValues21600() {
        return values21600;
    }
    public void setValues21600(List<List<Double>> values21600) {
        this.values21600 = values21600;
    }

    public List<List<Double>> getValues259200() {
        return values259200;
    }
    public void setValues259200(List<List<Double>> values259200) {
        this.values259200 = values259200;
    }
}
