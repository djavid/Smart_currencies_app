package com.djavid.bitcoinrate.model.realm

import com.djavid.bitcoinrate.model.cryptocompare.Datum
import com.djavid.bitcoinrate.model.project.ChartOption

import io.realm.RealmList
import io.realm.RealmObject


open class RealmHistoryData : RealmObject {

    var pair: String? = null
    var option: ChartOption? = null
    private var values: RealmList<Datum>? = null


    constructor()

    constructor(values: RealmList<Datum>, pair: String, option: ChartOption) {
        this.pair = pair
        this.values = values
        this.option = option
    }

    override fun toString(): String {
        return "RealmHistoryData{" +
                "values=" + values +
                "option=" + option!!.timespan +
                '}'.toString()
    }


    fun getValues(): RealmList<Datum> {
        return if (values == null) RealmList() else values

    }

    fun setValues(values: RealmList<Datum>) {
        this.values = values
    }

}
