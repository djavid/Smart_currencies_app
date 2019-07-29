package com.djavid.bitcoinrate.model.cryptocompare

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import io.realm.RealmObject


open class Datum : RealmObject {

    @SerializedName("time")
    @Expose
    var time: Long? = null

    @SerializedName("open")
    @Expose
    var open: Double? = null

    @SerializedName("close")
    @Expose
    var close: Double? = null

    @SerializedName("high")
    @Expose
    var high: Double? = null

    @SerializedName("low")
    @Expose
    var low: Double? = null

    @SerializedName("volumefrom")
    @Expose
    var volumefrom: Double? = null

    @SerializedName("volumeto")
    @Expose
    var volumeto: Double? = null


    constructor()

    constructor(time: Long?, open: Double?, close: Double?, high: Double?, low: Double?, volumefrom: Double?, volumeto: Double?) {
        this.time = time
        this.open = open
        this.close = close
        this.high = high
        this.low = low
        this.volumefrom = volumefrom
        this.volumeto = volumeto
    }

    override fun toString(): String {
        return "Datum{" +
                "time=" + time +
                ", open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", volumefrom=" + volumefrom +
                ", volumeto=" + volumeto +
                '}'.toString()
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null) return false

        val datum = o as Datum?

        if (if (time != null) time != datum!!.time else datum!!.time != null) return false
        if (if (open != null) open != datum.open else datum.open != null) return false
        if (if (close != null) close != datum.close else datum.close != null) return false
        if (if (high != null) high != datum.high else datum.high != null) return false
        if (if (low != null) low != datum.low else datum.low != null) return false
        if (if (volumefrom != null) volumefrom != datum.volumefrom else datum.volumefrom != null)
            return false
        return if (volumeto != null) volumeto == datum.volumeto else datum.volumeto == null
    }

    override fun hashCode(): Int {
        var result = if (time != null) time!!.hashCode() else 0
        result = 31 * result + if (open != null) open!!.hashCode() else 0
        result = 31 * result + if (close != null) close!!.hashCode() else 0
        result = 31 * result + if (high != null) high!!.hashCode() else 0
        result = 31 * result + if (low != null) low!!.hashCode() else 0
        result = 31 * result + if (volumefrom != null) volumefrom!!.hashCode() else 0
        result = 31 * result + if (volumeto != null) volumeto!!.hashCode() else 0
        return result
    }
}
