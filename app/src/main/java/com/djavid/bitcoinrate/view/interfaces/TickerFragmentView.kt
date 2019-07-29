package com.djavid.bitcoinrate.view.interfaces

import android.support.v4.widget.SwipeRefreshLayout

import com.djavid.bitcoinrate.core.ScrollView
import com.djavid.bitcoinrate.model.heroku.Subscribe
import com.djavid.bitcoinrate.model.heroku.Ticker


interface TickerFragmentView : ScrollView<Ticker> {
    val refreshLayout: SwipeRefreshLayout
    fun showSnackbar(message: String)
    fun addTickerToAdapter(ticker: Ticker)
    fun addAllTickers(tickers: List<Ticker>, subscribes: List<Subscribe>)
    fun updateRecyclerVisibility()
}
