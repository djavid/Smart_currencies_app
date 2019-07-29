package com.djavid.bitcoinrate.model.cryptocompare

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ConversionType {

    @SerializedName("type")
    @Expose
    var type: String

    @SerializedName("conversionSymbol")
    @Expose
    var conversionSymbol: String
}
