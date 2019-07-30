package com.djavid.bitcoinrate.extensions

import android.app.Activity
import org.kodein.di.KodeinAware

fun Activity.getKodein() = (applicationContext as KodeinAware).kodein