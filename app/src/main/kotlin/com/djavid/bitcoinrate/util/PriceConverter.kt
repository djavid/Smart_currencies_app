package com.djavid.bitcoinrate.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


object PriceConverter {

    fun convertMarketCap(market_cap: Double): String {

        if (market_cap < 1000000.0) {

            return "< 1 млн"

        } else if (market_cap >= 1000000.0 && market_cap < 10000000.0) {

            val x = Math.floor(market_cap / 1000000.0 * 10) / 10
            return java.lang.Double.toString(x) + " млн"

        } else if (market_cap >= 10000000.0 && market_cap < 1000000000.0) {

            val x = Math.round(Math.floor(market_cap / 1000000.0))
            return "$x млн"

        } else if (market_cap >= 1000000000.0 && market_cap < 10000000000.0) {

            val x = Math.floor(market_cap / 1000000000.0 * 100) / 100
            return java.lang.Double.toString(x) + " млрд"

        } else if (market_cap >= 10000000000.0 && market_cap < 1000000000000.0) {

            val x = Math.round(Math.floor(market_cap / 1000000000.0))
            return "$x млрд"

        } else if (market_cap >= 1000000000000.0) {

            val x = Math.floor(market_cap / 1000000000000.0 * 100) / 100
            return java.lang.Double.toString(x) + " трлн"

        } else {
            return ""
        }

    }

    fun convertPrice(price: Double): String {

        val symbols = DecimalFormatSymbols.getInstance()
        symbols.groupingSeparator = ' '

        val formatter = DecimalFormat(getPattern(price), symbols)

        return formatter.format(price)
    }

    private fun getPattern(price: Double?): String {

        return if (price < 1) {
            "###,###.######"
        } else if (price >= 1 && price < 10) {
            "###,###.###"
        } else if (price >= 10 && price < 100) {
            "###,###.##"
        } else if (price >= 100 && price < 1000) {
            "###,###.##"
        } else if (price >= 1000 && price < 10000) {
            "###,###.##"
        } else if (price >= 10000 && price < 100000) {
            "###,###.#"
        } else { //if (price > 100000)
            "###,###"
        }
    }

}
