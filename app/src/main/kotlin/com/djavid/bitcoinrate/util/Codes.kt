package com.djavid.bitcoinrate.util

import com.annimon.stream.IntStream
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.util.Config.country_coins
import com.djavid.bitcoinrate.util.Config.cryptoCoins

class Codes {
	
	fun getCryptoCoinIndex(id: String): Int {
		val cryptoTitles = cryptoCoins.map { it.id }.sorted().toList()
		
		return IntStream.range(0, cryptoTitles.size)
				.filter { i -> cryptoTitles[i] == id }
				.findFirst()
				.asInt
	}
	
	fun getCryptoCoinByCode(code: String) =
			cryptoCoins.firstOrNull { coin -> (coin.symbol == code) or (coin.id == code) }
	
	fun getCountryCoinByCode(code: String) = country_coins
			.firstOrNull { (it.symbol == code) or (it.id == code) }
	
	
	fun getCryptoCoinImage(code: String) =
			cryptoCoins.firstOrNull { (it.symbol == code) or (it.id == code) }?.imageUrl
	
	
	fun getCryptoCurrencyId(code: String) =
			cryptoCoins.firstOrNull { coin -> (coin.symbol == code) or (coin.id == code) }?.id
	
	fun getCountrySymbol(code: String): String = when (code) {
		"USD" -> "\u0024"
		"EUR" -> "\u20AC"
		"CAD" -> "C\u0024"
		"CNY" -> "\u00A5"
		"JPY" -> "\u00A5"
		"PLN" -> "z\u0142"
		"GBP" -> "\u00A3"
		"RUB" -> "\u20BD"
		"UAH" -> "\u20B4"
		else -> ""
	}
	
	fun getCountryImage(code: String) = when (code) {
		"RUB" -> R.drawable.russia
		"USD" -> R.drawable.usa
		"EUR" -> R.drawable.eu
		"CAD" -> R.drawable.canada
		"CNY" -> R.drawable.china
		"JPY" -> R.drawable.japan
		"PLN" -> R.drawable.poland
		"GBP" -> R.drawable.uk
		"UAH" -> R.drawable.ukraine
		else -> 0
	}
	
}
