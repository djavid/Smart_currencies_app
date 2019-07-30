package com.djavid.bitcoinrate.model.heroku


class ResponseId {

    var error: String
    var id: Long = 0


    constructor(id: Long) {
        this.id = id
        error = ""
    }

    constructor(error: String) {
        this.error = error
    }
}
