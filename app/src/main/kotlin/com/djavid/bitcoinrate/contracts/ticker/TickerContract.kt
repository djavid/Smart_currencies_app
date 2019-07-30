package com.djavid.bitcoinrate.contracts.ticker

import com.djavid.bitcoinrate.model.heroku.Subscribe
import com.djavid.bitcoinrate.model.heroku.Ticker
import com.djavid.bitcoinrate.view.ticker.TickerItem

interface TickerContract {
	
	interface Presenter {
		val tickers: List<Ticker>
		val subscribes: List<Subscribe>
		
		fun init()
		
		fun addTickerFromServer(token_id: Long, ticker_id: Long)
		fun getAllTickers(refresh: Boolean)
		fun deleteTicker(ticker_id: Long)
		fun loadTickerPrice(tickerItem: TickerItem)
		fun loadTickerPriceCMC(tickerItem: TickerItem)
		fun loadTickerPriceCryptonator(tickerItem: TickerItem)
		fun sortTickers(tickers: List<Ticker>): List<Ticker>
	}
	
	interface View {
		fun init(presenter: Presenter)
	}
	
}