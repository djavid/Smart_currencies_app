package com.djavid.bitcoinrate.model.dto.cryptowatch;

import com.djavid.bitcoinrate.model.dto.cryptowatch.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class HistoryDataModel {

    @SerializedName("result")
    @Expose
    private Result result;

    public Result getResult() {
        return result;
    }
    public void setResult(Result result) {
        this.result = result;
    }

}
