package com.djavid.bitcoinrate.model.project


class LabelItemDto {

    var id: Long? = null
    var value: String? = null
    var isTrendingUp: Boolean = false
    var isAddButton: Boolean = false
    var change_percent: Double = 0.toDouble()
    var isPercentLabel: Boolean = false


    constructor() {
        this.isAddButton = true
    }

    constructor(id: Long, value: String, isTrendingUp: Boolean, change_percent: Double) {
        this.id = id
        this.value = value
        this.isTrendingUp = isTrendingUp
        this.change_percent = change_percent
        this.isAddButton = false

        if (change_percent > 0) isPercentLabel = true
    }

    constructor(value: String, isTrendingUp: Boolean, isPercentLabel: Boolean) {
        this.isTrendingUp = isTrendingUp
        this.isPercentLabel = isPercentLabel

        if (isPercentLabel) {
            this.change_percent = java.lang.Double.parseDouble(value)
        } else {
            this.value = value
        }
    }
}
