package com.djavid.bitcoinrate.di

import android.view.View
import androidx.fragment.app.Fragment
import com.djavid.bitcoinrate.TICKERS_MODULE
import kotlinx.android.synthetic.main.fragment_ticker.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class TickersFragmentModule(fragment: Fragment) {
	val kodein = Kodein.Module(TICKERS_MODULE) {
		bind<View>() with singleton { fragment.tickersFragment }
	}
}