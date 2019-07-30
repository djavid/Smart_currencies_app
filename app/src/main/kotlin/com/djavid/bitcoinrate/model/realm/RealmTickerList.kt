package com.djavid.bitcoinrate.model.realm

import com.djavid.bitcoinrate.model.heroku.Ticker

import io.realm.RealmList
import io.realm.RealmObject


open class RealmTickerList : RealmObject {

    var list: RealmList<Ticker>? = null


    constructor()

    constructor(list: List<Ticker>) {
        this.list = RealmList(*list.toTypedArray())
    }

    override fun toString(): String {
        return "RealmTickerList{" +
                "list=" + list +
                '}'.toString()
    }
}
