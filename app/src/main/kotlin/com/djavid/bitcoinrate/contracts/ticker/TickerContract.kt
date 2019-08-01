package com.djavid.bitcoinrate.contracts.ticker

import android.content.Intent
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.djavid.bitcoinrate.model.heroku.Subscribe
import com.djavid.bitcoinrate.model.heroku.Ticker
import com.djavid.bitcoinrate.view.ticker.TickerItem

interface TickerContract {
	
	interface Presenter {
		fun init()
		fun addTickerFromServer(token_id: Long, ticker_id: Long)
		fun getAllTickers(refresh: Boolean)
		fun deleteTicker(ticker_id: Long)
		fun loadTickerPrice(tickerItem: TickerItem)
		fun loadTickerPriceCMC(tickerItem: TickerItem)
		fun loadTickerPriceCryptonator(tickerItem: TickerItem)
		fun sortTickers(tickers: List<Ticker>): List<Ticker>
		fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent)
		fun showPopupWindow()
		fun onDestroy()
	}
	
	interface View {
		fun init(presenter: Presenter)
		fun showPopupWindow()
		fun scrollToPosition(position: Int)
		fun resetFeed()
		val refreshLayout: SwipeRefreshLayout
		fun showSnackbar(message: String)
		fun addTickerToAdapter(ticker: Ticker)
		fun addAllTickers(tickers: List<Ticker>, subscribes: List<Subscribe>)
		fun updateRecyclerVisibility()
		fun showError(error: String)
		fun showError(res: Int)
		fun setRefreshing(key: Boolean)
	}
	
}