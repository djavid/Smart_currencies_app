package com.djavid.bitcoinrate.model.project

import com.djavid.bitcoinrate.App
import com.djavid.bitcoinrate.util.Config

import ir.mirrajabi.searchdialog.core.Searchable


class Coin : Searchable {

    var symbol: String
    var id: String
    var imageUrl: String

    constructor(symbol: String, id: String) {
        this.symbol = symbol
        this.id = id
        this.imageUrl = ""
    }

    constructor(symbol: String, id: String, imageUrl: String) {
        this.symbol = symbol
        this.id = id
        this.imageUrl = Config.IMAGE_BASE_URL + imageUrl
    }

    override fun getTitle(): String {
        return if (App.appInstance!!.preferences.titleFormat == "codes")
            symbol
        else
            id
    }


}
