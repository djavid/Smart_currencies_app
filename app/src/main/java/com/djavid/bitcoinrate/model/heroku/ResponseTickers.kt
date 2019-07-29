package com.djavid.bitcoinrate.model.heroku


class ResponseTickers {

    var error: String
    var tickers: List<Ticker>


    constructor(error: String) {
        this.error = error
    }

    constructor(error: String, tickers: List<Ticker>) {
        this.error = error
        this.tickers = tickers
    }

    constructor(tickers: List<Ticker>) {
        this.tickers = tickers
        error = ""
    }

}
