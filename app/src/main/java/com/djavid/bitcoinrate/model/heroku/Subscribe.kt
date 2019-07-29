package com.djavid.bitcoinrate.model.heroku

import io.realm.RealmObject


open class Subscribe : RealmObject {

    var id: Long? = null
    var isTrendingUp: Boolean = false
    var value: String? = null
    var tickerId: Long = 0
    var tokenId: Long = 0
    var cryptoId: String? = null
    var countryId: String? = null
    var change_percent: Double = 0.toDouble()


    constructor()

    constructor(isTrendingUp: Boolean, value: String, tickerId: Long, tokenId: Long, cryptoId: String, countryId: String) {

        this.isTrendingUp = isTrendingUp
        this.value = value
        this.tickerId = tickerId
        this.tokenId = tokenId
        this.cryptoId = cryptoId
        this.countryId = countryId

        this.change_percent = 0.0
    }

    constructor(value: String, tickerId: Long, tokenId: Long, cryptoId: String, countryId: String, curr_price: Double) {

        this.value = java.lang.Double.toString(curr_price)
        this.tickerId = tickerId
        this.tokenId = tokenId
        this.cryptoId = cryptoId
        this.countryId = countryId
        this.change_percent = java.lang.Double.parseDouble(value)

        this.isTrendingUp = false
    }

    override fun toString(): String {
        return "Subscribe{" +
                "id=" + id +
                ", isTrendingUp=" + isTrendingUp +
                ", value='" + value + '\''.toString() +
                ", tickerId=" + tickerId +
                ", tokenId=" + tokenId +
                ", cryptoId='" + cryptoId + '\''.toString() +
                ", countryId='" + countryId + '\''.toString() +
                ", change_percent=" + change_percent +
                '}'.toString()
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null) return false

        val subscribe = o as Subscribe?

        if (isTrendingUp != subscribe!!.isTrendingUp) return false
        if (tickerId != subscribe.tickerId) return false
        if (tokenId != subscribe.tokenId) return false
        if (java.lang.Double.compare(subscribe.change_percent, change_percent) != 0) return false
        if (if (id != null) id != subscribe.id else subscribe.id != null) return false
        if (if (value != null) value != subscribe.value else subscribe.value != null) return false
        if (if (cryptoId != null) cryptoId != subscribe.cryptoId else subscribe.cryptoId != null)
            return false
        return if (countryId != null) countryId == subscribe.countryId else subscribe.countryId == null
    }

    override fun hashCode(): Int {
        var result: Int
        val temp: Long
        result = if (id != null) id!!.hashCode() else 0
        result = 31 * result + if (isTrendingUp) 1 else 0
        result = 31 * result + if (value != null) value!!.hashCode() else 0
        result = 31 * result + (tickerId xor tickerId.ushr(32)).toInt()
        result = 31 * result + (tokenId xor tokenId.ushr(32)).toInt()
        result = 31 * result + if (cryptoId != null) cryptoId!!.hashCode() else 0
        result = 31 * result + if (countryId != null) countryId!!.hashCode() else 0
        temp = java.lang.Double.doubleToLongBits(change_percent)
        result = 31 * result + (temp xor temp.ushr(32)).toInt()
        return result
    }
}
