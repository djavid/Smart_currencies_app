package com.djavid.bitcoinrate.model.realm

import com.djavid.bitcoinrate.model.heroku.Subscribe

import io.realm.RealmList
import io.realm.RealmObject


open class RealmSubscribeList : RealmObject {

    var list: RealmList<Subscribe>? = null


    constructor()

    constructor(list: List<Subscribe>) {
        this.list = RealmList(*list.toTypedArray())
    }

    override fun toString(): String {
        return "RealmSubscribeList{" +
                "list=" + list +
                '}'.toString()
    }
}
