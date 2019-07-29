package com.djavid.bitcoinrate.model.cryptonator


class CryptonatorTicker {

    var ticker: Ticker? = null
    var timestamp: Long? = null
    var success: Boolean? = null
    var error: String? = null


    constructor()

    constructor(ticker: Ticker, timestamp: Long?, success: Boolean?, error: String) : super() {
        this.ticker = ticker
        this.timestamp = timestamp
        this.success = success
        this.error = error
    }

}
