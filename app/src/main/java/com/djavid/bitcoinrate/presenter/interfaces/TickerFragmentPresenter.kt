package com.djavid.bitcoinrate.presenter.interfaces

import com.djavid.bitcoinrate.core.Presenter
import com.djavid.bitcoinrate.core.Router
import com.djavid.bitcoinrate.model.heroku.Subscribe
import com.djavid.bitcoinrate.model.heroku.Ticker
import com.djavid.bitcoinrate.view.adapter.TickerItem
import com.djavid.bitcoinrate.view.interfaces.TickerFragmentView


interface TickerFragmentPresenter : Presenter<TickerFragmentView, Router, Any> {
    val tickers: List<Ticker>
    val subscribes: List<Subscribe>

    fun addTickerFromServer(token_id: Long, ticker_id: Long)
    fun getAllTickers(refresh: Boolean)
    fun deleteTicker(ticker_id: Long)
    fun loadTickerPrice(tickerItem: TickerItem)
    fun loadTickerPriceCMC(tickerItem: TickerItem)
    fun loadTickerPriceCryptonator(tickerItem: TickerItem)
    fun sortTickers(tickers: List<Ticker>): List<Ticker>
}
