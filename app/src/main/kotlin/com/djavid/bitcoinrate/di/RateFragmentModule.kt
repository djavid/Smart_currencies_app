package com.djavid.bitcoinrate.di

import android.view.View
import androidx.fragment.app.Fragment
import com.djavid.bitcoinrate.RATE_CHART_VIEW
import com.djavid.bitcoinrate.RATE_MODULE
import com.djavid.bitcoinrate.contracts.rate.RateContract
import com.djavid.bitcoinrate.contracts.rate.RatePresenter
import com.djavid.bitcoinrate.view.rate.RateChartView
import com.djavid.bitcoinrate.view.rate.RateView
import kotlinx.android.synthetic.main.fragment_rate.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class RateFragmentModule(fragment: Fragment) {
	val kodein = Kodein.Module(RATE_MODULE) {
		bind<View>() with singleton { fragment.rateFragment }
		bind<View>(RATE_CHART_VIEW) with singleton { fragment.chart }
		bind<RateContract.View>() with singleton { RateView(instance()) }
		bind<RateContract.Presenter>() with singleton { RatePresenter(instance(), instance(), instance()) }
		bind<RateContract.ChartView>() with singleton { RateChartView(instance(RATE_CHART_VIEW)) }
	}
}