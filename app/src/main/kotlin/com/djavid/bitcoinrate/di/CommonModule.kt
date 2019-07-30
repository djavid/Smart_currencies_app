package com.djavid.bitcoinrate.di

import android.content.Context
import com.djavid.bitcoinrate.COMMON_MODULE
import com.djavid.bitcoinrate.SHARED_PREFERENCES_NAME
import com.djavid.bitcoinrate.util.SavedPreferences
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class CommonModule(private val appContext: Context) {
	
	private val sharedPreferences = appContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
	
	val kodein = Kodein.Module(COMMON_MODULE) {
		bind<SavedPreferences>() with singleton { SavedPreferences(sharedPreferences) }
	}
	
}