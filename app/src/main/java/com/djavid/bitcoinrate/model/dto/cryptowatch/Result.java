package com.djavid.bitcoinrate.model.dto.cryptowatch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Result {

    @SerializedName("86400")
    @Expose
    private List<List<Double>> values86400 = null;

    @SerializedName("43200")
    @Expose
    private List<List<Double>> values43200 = null;

    @SerializedName("21600")
    @Expose
    private List<List<Double>> values21600 = null;

    @SerializedName("7200")
    @Expose
    private List<List<Double>> values7200 = null;

    public List<List<Double>> getValues() {
        if (getValues7200() != null) return getValues7200();
        if (getValues21600() != null) return getValues21600();
        if (getValues43200() != null) return getValues43200();
        if (getValues86400() != null) return getValues86400();

        return getValues86400();
    }

    private List<List<Double>> getValues86400() {
        return values86400;
    }
    private void setValues86400(List<List<Double>> values86400) {
        this.values86400 = values86400;
    }

    private List<List<Double>> getValues43200() {
        return values43200;
    }
    private void setValues43200(List<List<Double>> values43200) {
        this.values43200 = values43200;
    }

    private List<List<Double>> getValues21600() {
        return values21600;
    }
    private void setValues21600(List<List<Double>> values21600) {
        this.values21600 = values21600;
    }

    private List<List<Double>> getValues7200() {
        return values7200;
    }
    private void setValues7200(List<List<Double>> values7200) {
        this.values7200 = values7200;
    }
}
