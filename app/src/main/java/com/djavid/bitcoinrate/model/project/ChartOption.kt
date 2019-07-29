package com.djavid.bitcoinrate.model.project

import io.realm.RealmObject


open class ChartOption : RealmObject {

    var timespan: String? = null
    var days: Int = 0
    var intervals: Int = 0

    var limit: Int = 0
    var periods: Int = 0
    var request: String? = null


    constructor()

    constructor(timespan: String, days: Int, intervals: Int,
                limit: Int, periods: Int, request: String) {
        this.timespan = timespan
        this.days = days
        this.intervals = intervals
        this.limit = limit
        this.periods = periods
        this.request = request
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null) return false

        val that = o as ChartOption?

        if (days != that!!.days) return false
        if (intervals != that.intervals) return false
        if (limit != that.limit) return false
        if (periods != that.periods) return false
        if (if (timespan != null) timespan != that.timespan else that.timespan != null)
            return false
        return if (request != null) request == that.request else that.request == null
    }

    override fun hashCode(): Int {
        var result = if (timespan != null) timespan!!.hashCode() else 0
        result = 31 * result + days
        result = 31 * result + intervals
        result = 31 * result + limit
        result = 31 * result + periods
        result = 31 * result + if (request != null) request!!.hashCode() else 0
        return result
    }
}
