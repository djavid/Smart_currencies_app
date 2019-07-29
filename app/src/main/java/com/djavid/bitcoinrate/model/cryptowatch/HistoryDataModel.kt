package com.djavid.bitcoinrate.model.cryptowatch

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class HistoryDataModel {

    @SerializedName("result")
    @Expose
    var result: Result? = null

}
