package com.djavid.bitcoinrate.model.cryptonator

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CurrenciesModel {

    @SerializedName("rows")
    @Expose
    var rows: List<Row>? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor()

    /**
     *
     * @param rows
     */
    constructor(rows: List<Row>) : super() {
        this.rows = rows
    }

}
