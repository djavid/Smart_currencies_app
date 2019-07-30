package com.djavid.bitcoinrate.contracts.main

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.djavid.bitcoinrate.MAIN_MODULE
import com.djavid.bitcoinrate.app.KodeinApp
import com.djavid.bitcoinrate.view.main.MainPagerAdapter
import com.djavid.bitcoinrate.view.main.MainView
import com.djavid.bitcoinrate.view.rate.RateFragment
import com.djavid.bitcoinrate.view.settings.SettingsFragment
import com.djavid.bitcoinrate.view.ticker.TickerFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class MainActivityModule(activity: Activity) {
	val kodein = Kodein.Module(MAIN_MODULE) {
		bind<KodeinApp>() with singleton { context as KodeinApp }
		bind<View>() with singleton { activity.mainActivity }
		bind<FragmentManager>() with singleton { (activity as AppCompatActivity).supportFragmentManager }
		bind<MainContract.View>() with singleton { MainView(instance(), instance(), instance()) }
		bind<MainContract.Presenter>() with singleton { MainPresenter(instance(), instance(), instance()) }
		bind<List<Fragment>>() with singleton { listOf(RateFragment(), TickerFragment(), SettingsFragment()) }
		bind<MainPagerAdapter>() with singleton { MainPagerAdapter(instance(), instance()) }
	}
}