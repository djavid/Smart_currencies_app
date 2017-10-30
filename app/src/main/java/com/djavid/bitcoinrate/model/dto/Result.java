package com.djavid.bitcoinrate.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Result {

    @SerializedName("86400")
    @Expose
    private List<List<Double>> values = null;

    public List<List<Double>> getValues() {
        return values;
    }
    public void setValues(List<List<Double>> values) {
        this.values = values;
    }
}
