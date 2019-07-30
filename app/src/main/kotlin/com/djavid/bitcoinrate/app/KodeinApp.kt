package com.djavid.bitcoinrate.app

import android.app.Activity
import androidx.fragment.app.Fragment
import org.kodein.di.Kodein

interface KodeinApp {
	fun mainComponent(activity: Activity): Kodein
	fun rateComponent(fragment: Fragment): Kodein
	fun tickerComponent(fragment: Fragment): Kodein
	fun settingsFragmentComponent(fragment: Fragment): Kodein
}