package com.djavid.bitcoinrate.model.blockchain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Value {

    @SerializedName("x")
    @Expose
    var x: Long = 0
    @SerializedName("y")
    @Expose
    var y: Double = 0.toDouble()

    /**
     * No args constructor for use in serialization
     *
     */
    constructor()

    /**
     *
     * @param y
     * @param x
     */
    constructor(x: Long, y: Double) : super() {
        this.x = x
        this.y = y
    }

}
