package com.djavid.bitcoinrate.model.heroku

import io.realm.RealmObject


open class Ticker : RealmObject {

    var id: Long = 0
    var tokenId: Long = 0
    var cryptoId: String? = null
    var countryId: String? = null
    var ticker: CurrencyUpdate? = null


    constructor()

    constructor(tokenId: Long, cryptoId: String, countryId: String) {
        this.tokenId = tokenId
        this.cryptoId = cryptoId
        this.countryId = countryId
    }

    constructor(id: Long, tokenId: Long, cryptoId: String, countryId: String) {
        this.id = id
        this.tokenId = tokenId
        this.cryptoId = cryptoId
        this.countryId = countryId
    }


    override fun toString(): String {
        return "Ticker{" +
                "id=" + id +
                ", tokenId=" + tokenId +
                ", cryptoId='" + cryptoId + '\''.toString() +
                ", countryId='" + countryId + '\''.toString() +
                ", ticker=" + ticker +
                '}'.toString()
    }


    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null) return false

        val ticker1 = o as Ticker?

        if (id != ticker1!!.id) return false
        if (tokenId != ticker1.tokenId) return false
        if (if (cryptoId != null) cryptoId != ticker1.cryptoId else ticker1.cryptoId != null)
            return false
        if (if (countryId != null) countryId != ticker1.countryId else ticker1.countryId != null)
            return false
        return if (ticker != null) ticker == ticker1.ticker else ticker1.ticker == null
    }

    override fun hashCode(): Int {
        var result = (id xor id.ushr(32)).toInt()
        result = 31 * result + (tokenId xor tokenId.ushr(32)).toInt()
        result = 31 * result + if (cryptoId != null) cryptoId!!.hashCode() else 0
        result = 31 * result + if (countryId != null) countryId!!.hashCode() else 0
        result = 31 * result + if (ticker != null) ticker!!.hashCode() else 0
        return result
    }

}
