package com.djavid.bitcoinrate.view.ticker

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.djavid.bitcoinrate.model.heroku.Subscribe
import com.djavid.bitcoinrate.model.heroku.Ticker

interface TickerFragmentView {
	fun scrollToPosition(position: Int)
	fun resetFeed()
	val refreshLayout: SwipeRefreshLayout
	fun showSnackbar(message: String)
	fun addTickerToAdapter(ticker: Ticker)
	fun addAllTickers(tickers: List<Ticker>, subscribes: List<Subscribe>)
	fun updateRecyclerVisibility()
}
