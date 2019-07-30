package com.djavid.bitcoinrate.model.heroku

import io.realm.RealmObject


open class CurrencyUpdate : RealmObject() {

    var id: Long = 0
    var cryptoId: String? = null
    var countryId: String? = null
    var price: Double = 0.toDouble()

    var rank: Int = 0
    var market_cap_usd: Double = 0.toDouble()
    var available_supply: Double = 0.toDouble()
    var total_supply: Double = 0.toDouble()
    var percent_change_1h: Double = 0.toDouble()
    var percent_change_24h: Double = 0.toDouble()
    var percent_change_7d: Double = 0.toDouble()
    var last_updated: Long = 0


    override fun toString(): String {
        return "CurrencyUpdate{" +
                "id=" + id +
                ", cryptoId='" + cryptoId + '\''.toString() +
                ", countryId='" + countryId + '\''.toString() +
                ", price=" + price +
                ", rank=" + rank +
                ", market_cap_usd=" + market_cap_usd +
                ", available_supply=" + available_supply +
                ", total_supply=" + total_supply +
                ", percent_change_1h=" + percent_change_1h +
                ", percent_change_24h=" + percent_change_24h +
                ", percent_change_7d=" + percent_change_7d +
                ", last_updated=" + last_updated +
                '}'.toString()
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null) return false

        val that = o as CurrencyUpdate?

        if (id != that!!.id) return false
        if (java.lang.Double.compare(that.price, price) != 0) return false
        if (rank != that.rank) return false
        if (java.lang.Double.compare(that.market_cap_usd, market_cap_usd) != 0) return false
        if (java.lang.Double.compare(that.available_supply, available_supply) != 0) return false
        if (java.lang.Double.compare(that.total_supply, total_supply) != 0) return false
        if (java.lang.Double.compare(that.percent_change_1h, percent_change_1h) != 0) return false
        if (java.lang.Double.compare(that.percent_change_24h, percent_change_24h) != 0) return false
        if (java.lang.Double.compare(that.percent_change_7d, percent_change_7d) != 0) return false
        if (last_updated != that.last_updated) return false
        if (if (cryptoId != null) cryptoId != that.cryptoId else that.cryptoId != null)
            return false
        return if (countryId != null) countryId == that.countryId else that.countryId == null
    }

    override fun hashCode(): Int {
        var result: Int
        var temp: Long
        result = (id xor id.ushr(32)).toInt()
        result = 31 * result + if (cryptoId != null) cryptoId!!.hashCode() else 0
        result = 31 * result + if (countryId != null) countryId!!.hashCode() else 0
        temp = java.lang.Double.doubleToLongBits(price)
        result = 31 * result + (temp xor temp.ushr(32)).toInt()
        result = 31 * result + rank
        temp = java.lang.Double.doubleToLongBits(market_cap_usd)
        result = 31 * result + (temp xor temp.ushr(32)).toInt()
        temp = java.lang.Double.doubleToLongBits(available_supply)
        result = 31 * result + (temp xor temp.ushr(32)).toInt()
        temp = java.lang.Double.doubleToLongBits(total_supply)
        result = 31 * result + (temp xor temp.ushr(32)).toInt()
        temp = java.lang.Double.doubleToLongBits(percent_change_1h)
        result = 31 * result + (temp xor temp.ushr(32)).toInt()
        temp = java.lang.Double.doubleToLongBits(percent_change_24h)
        result = 31 * result + (temp xor temp.ushr(32)).toInt()
        temp = java.lang.Double.doubleToLongBits(percent_change_7d)
        result = 31 * result + (temp xor temp.ushr(32)).toInt()
        result = 31 * result + (last_updated xor last_updated.ushr(32)).toInt()
        return result
    }


    fun getPercentChange(setting: String): Double {

        when (setting) {
            "hour" -> return percent_change_1h
            "day" -> return percent_change_24h
            "week" -> return percent_change_7d
            else -> return percent_change_24h
        }
    }
}
