package com.djavid.bitcoinrate.model.cryptonator

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Row {

    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("statuses")
    @Expose
    var statuses: List<String>? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor()

    /**
     *
     * @param name
     * @param code
     * @param statuses
     */
    constructor(code: String, name: String, statuses: List<String>) : super() {
        this.code = code
        this.name = name
        this.statuses = statuses
    }

}
