package com.djavid.bitcoinrate.model.cryptocompare

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class HistoData {

    @SerializedName("Response")
    @Expose
    var response: String

    @SerializedName("Message")
    @Expose
    var message: String

    @SerializedName("Type")
    @Expose
    var type: Int? = null

    @SerializedName("Aggregated")
    @Expose
    var aggregated: Boolean? = null

    @SerializedName("Data")
    @Expose
    var data: List<Datum>? = null

    @SerializedName("TimeTo")
    @Expose
    var timeTo: Long = 0

    @SerializedName("TimeFrom")
    @Expose
    var timeFrom: Long = 0

    @SerializedName("ConversionType")
    @Expose
    var conversionType: ConversionType

}
