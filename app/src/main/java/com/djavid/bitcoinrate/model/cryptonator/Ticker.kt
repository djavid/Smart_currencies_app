package com.djavid.bitcoinrate.model.cryptonator


class Ticker {

    var base: String? = null
    var target: String? = null
    var price: Double = 0.toDouble()
    var volume: String? = null
    var change: Double = 0.toDouble()
    var markets: List<Market>? = null


    constructor()

    constructor(base: String, target: String, price: Double, volume: String, change: Double, markets: List<Market>) : super() {
        this.base = base
        this.target = target
        this.price = price
        this.volume = volume
        this.change = change
        this.markets = markets
    }

}
