package com.djavid.bitcoinrate.view.main

import android.app.Activity
import android.os.Bundle
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.app.KodeinApp
import com.djavid.bitcoinrate.contracts.main.MainContract
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class MainActivity : Activity(), KodeinAware {
	
	override lateinit var kodein: Kodein
	
	private val presenter: MainContract.Presenter by instance()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		
		kodein = (application as KodeinApp).mainComponent(this)
		presenter.init()
	}
	
}
