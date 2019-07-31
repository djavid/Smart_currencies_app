package com.djavid.bitcoinrate.view.ticker

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.app.KodeinApp
import com.djavid.bitcoinrate.base.BaseFragment
import com.djavid.bitcoinrate.contracts.ticker.TickerContract
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class TickerFragment : BaseFragment(), KodeinAware, SwipeRefreshLayout.OnRefreshListener {
	
	private val presenter: TickerContract.Presenter by instance()
	
	override lateinit var kodein: Kodein
	
	override val layoutRes = R.layout.fragment_ticker
	
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
		
		kodein = (activity?.applicationContext as KodeinApp).tickerComponent(this)
		presenter.init()
	}
	
	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		super.onCreateOptionsMenu(menu, inflater)
		inflater.inflate(R.menu.menu_tickers, menu)
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.refresh -> onRefresh()
			R.id.sort -> presenter.showPopupWindow()
		}
		
		return super.onOptionsItemSelected(item)
	}
	
	override fun onRefresh() {
		presenter.getAllTickers(true)
	}
	
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
		presenter.onActivityResult(requestCode, resultCode, data)
	}
	
}
