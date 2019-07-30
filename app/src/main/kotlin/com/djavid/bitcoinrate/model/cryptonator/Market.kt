package com.djavid.bitcoinrate.model.cryptonator


class Market {

    var market: String? = null
    var price: Double? = null
    var volume: Double? = null


    constructor()

    constructor(market: String, price: Double?, volume: Double?) : super() {
        this.market = market
        this.price = price
        this.volume = volume
    }

}
