package com.djavid.bitcoinrate.view.rate

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.app.KodeinApp
import com.djavid.bitcoinrate.base.BaseFragment
import com.djavid.bitcoinrate.contracts.rate.RateContract

class RateFragment : BaseFragment() {
	
	lateinit var presenter: RateContract.Presenter
	
	override val layoutRes = R.layout.fragment_rate
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		(activity as KodeinApp).rateComponent(this)
		presenter.init()
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}
	
	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		super.onCreateOptionsMenu(menu, inflater)
		inflater.inflate(R.menu.menu_rate, menu)
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		presenter.onOptionsItemSelected(item.itemId)
		return super.onOptionsItemSelected(item)
	}
	
}












