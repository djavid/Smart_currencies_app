package com.djavid.bitcoinrate.model.blockchain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class BlockchainModel {

    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("unit")
    @Expose
    var unit: String? = null
    @SerializedName("period")
    @Expose
    var period: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("values")
    @Expose
    var values: List<Value>? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor()

    /**
     *
     * @param unit
     * @param values
     * @param status
     * @param description
     * @param name
     * @param period
     */
    constructor(status: String, name: String, unit: String, period: String, description: String, values: List<Value>) : super() {
        this.status = status
        this.name = name
        this.unit = unit
        this.period = period
        this.description = description
        this.values = values
    }

}
