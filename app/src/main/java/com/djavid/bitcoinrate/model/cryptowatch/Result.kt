package com.djavid.bitcoinrate.model.cryptowatch

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Result {

    @SerializedName("900")
    @Expose
    var values900: List<List<Double>>? = null

    @SerializedName("3600")
    @Expose
    var values3600: List<List<Double>>? = null

    @SerializedName("7200")
    @Expose
    private var values7200: List<List<Double>>? = null

    @SerializedName("21600")
    @Expose
    var values21600: List<List<Double>>? = null

    @SerializedName("86400")
    @Expose
    private var values86400: List<List<Double>>? = null

    @SerializedName("259200")
    @Expose
    var values259200: List<List<Double>>? = null


    val values: List<List<Double>>?
        get() {

            if (values900 != null) return values900
            if (values3600 != null) return values3600
            if (values7200 != null) return values7200
            if (values21600 != null) return values21600
            if (values86400 != null) return values86400
            return if (values259200 != null) values259200 else values86400

        }
}
