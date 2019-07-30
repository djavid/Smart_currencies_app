package com.djavid.bitcoinrate.contracts.main

import com.anjlab.android.iab.v3.BillingProcessor
import com.djavid.bitcoinrate.util.SavedPreferences
import com.djavid.bitcoinrate.view.ticker.TickerItem

class MainPresenter(
		private val view: MainContract.View,
		private val billingProcessor: BillingProcessor,
		private val preferences: SavedPreferences
) : MainContract.Presenter {
	
	private val TAG = javaClass::class.java.simpleName
	
	private val subscribesAmount = preferences.subscribesAmount
	
	override fun init() {
		view.init(this)
		
	}
	
	private var selectedTickerItem: TickerItem? = null

//	todo add purchase
//	fun purchase() {
//
//		val isAvailable = BillingProcessor.isIabServiceAvailable(this)
//
//		if (isAvailable) {
//
//			val isSubsUpdateSupported = billingProcessor!!.isSubscriptionUpdateSupported
//
//			if (isSubsUpdateSupported) {
//				billingProcessor!!.subscribe(this, Config.PURCHASE_PRODUCT_ID)
//			} else {
//				showError(R.string.error_service_unavailable)
//			}
//
//		} else {
//			showError(R.string.error_service_unavailable)
//		}
//
//	}
	
	
}