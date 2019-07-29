package com.djavid.bitcoinrate.util

import com.annimon.stream.Collectors
import com.annimon.stream.IntStream
import com.annimon.stream.Stream
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.model.project.Coin

import com.djavid.bitcoinrate.util.Config.country_coins
import com.djavid.bitcoinrate.util.Config.crypto_coins


object Codes {

    private val crypto_coins_array_titles = Stream
            .of(*crypto_coins)
            .map { coin -> coin.id }
            .sorted()
            .collect<List<String>, Any>(Collectors.toList())
            .toTypedArray()


    fun getCryptoCoinIndex(id: String): Int {

        return IntStream.range(0, crypto_coins_array_titles.size)
                .filter { i -> crypto_coins_array_titles[i] == id }
                .findFirst()
                .asInt
    }

    fun getCryptoCoinByCode(code: String): Coin {
        return Stream.of(*crypto_coins)
                .filter { coin -> (coin.symbol == code) or (coin.id == code) }
                .findFirst()
                .get()
    }

    fun getCountryCoinByCode(code: String): Coin {
        return Stream.of(*country_coins)
                .filter { coin -> (coin.symbol == code) or (coin.id == code) }
                .findFirst()
                .get()
    }

    fun getCryptoCoinImage(code: String): String {
        return Stream.of(*crypto_coins)
                .filter { coin -> (coin.symbol == code) or (coin.id == code) }
                .findFirst()
                .get()
                .imageUrl
    }

    fun getCryptoCurrencyId(code: String): String {

        return Stream.of(*crypto_coins)
                .filter { coin -> (coin.symbol == code) or (coin.id == code) }
                .findFirst()
                .get().id
    }

    fun getCountrySymbol(code: String): String {

        when (code) {
            "USD" -> return "\u0024"
            "EUR" -> return "\u20AC"
            "CAD" -> return "C\u0024"
            "CNY" -> return "\u00A5"
            "JPY" -> return "\u00A5"
            "PLN" -> return "z\u0142"
            "GBP" -> return "\u00A3"
            "RUB" -> return "\u20BD"
            "UAH" -> return "\u20B4"
            else -> return ""
        }
    }

    fun getCountryImage(code: String): Int {
        when (code) {
            "RUB" -> return R.drawable.russia
            "USD" -> return R.drawable.usa
            "EUR" -> return R.drawable.eu
            "CAD" -> return R.drawable.canada
            "CNY" -> return R.drawable.china
            "JPY" -> return R.drawable.japan
            "PLN" -> return R.drawable.poland
            "GBP" -> return R.drawable.uk
            "UAH" -> return R.drawable.ukraine

            else -> return 0
        }
    }

}
