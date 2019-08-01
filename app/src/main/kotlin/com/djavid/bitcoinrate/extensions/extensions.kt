package com.djavid.bitcoinrate.extensions

import android.app.Activity
import android.view.View
import org.kodein.di.KodeinAware

fun Activity.getKodein() = (applicationContext as KodeinAware).kodein

fun View.show(show: Boolean) {
	visibility = if (show) View.VISIBLE else View.GONE
}