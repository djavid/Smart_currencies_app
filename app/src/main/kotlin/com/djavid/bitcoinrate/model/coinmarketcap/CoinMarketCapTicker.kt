package com.djavid.bitcoinrate.model.coinmarketcap


class CoinMarketCapTicker {

    var id: String? = null
    var name: String? = null
    var symbol: String? = null
    var rank: Int = 0
    var market_cap_usd: Double = 0.toDouble()
    var available_supply: Double = 0.toDouble()
    var total_supply: Double = 0.toDouble()
    var percent_change_1h: Double = 0.toDouble()
    var percent_change_24h: Double = 0.toDouble()
    var percent_change_7d: Double = 0.toDouble()
    var last_updated: Long = 0

    var price_btc: Double = 0.toDouble()
    var price_usd: Double = 0.toDouble()
    var price_eur: Double = 0.toDouble()
    var price_cad: Double = 0.toDouble()
    var price_cny: Double = 0.toDouble()
    var price_jpy: Double = 0.toDouble()
    var price_pln: Double = 0.toDouble()
    var price_gbp: Double = 0.toDouble()
    var price_rub: Double = 0.toDouble()
    var price_uah: Double = 0.toDouble()


    fun getPrice(id: String): Double? {
        when (id) {
            "USD" -> return price_usd
            "EUR" -> return price_eur

            "CAD" -> return price_cad
            "CNY" -> return price_cny
            "JPY" -> return price_jpy
            "PLN" -> return price_pln
            "GBP" -> return price_gbp
            "RUB" -> return price_rub
            "UAH" -> return price_uah
            else -> return 0.0
        }
    }

    fun getPercentChange(setting: String): Double {
        when (setting) {
            "hour" -> return percent_change_1h
            "day" -> return percent_change_24h
            "week" -> return percent_change_7d
            else -> return percent_change_24h
        }
    }
}