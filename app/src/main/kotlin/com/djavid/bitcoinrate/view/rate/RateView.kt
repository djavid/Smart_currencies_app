package com.djavid.bitcoinrate.view.rate

import android.view.View
import com.bumptech.glide.Glide
import com.djavid.bitcoinrate.contracts.rate.RateContract
import com.djavid.bitcoinrate.util.Codes
import kotlinx.android.synthetic.main.fragment_rate.view.*

class RateView(
		private val viewRoot: View
) : RateContract.View {
	
	private lateinit var presenter: RateContract.Presenter
	
	override fun init(presenter: RateContract.Presenter) {
		this.presenter = presenter
		setOnClickListeners()
	}
	
	private fun setOnClickListeners() {
		val options = listOf(viewRoot.optionFirst, viewRoot.optionSecond,
				viewRoot.optionThird, viewRoot.optionFourth)
		
		options.forEach { it.setOnClickListener { presenter.onChartOptionClick() } }
	}
	
	override fun setCurrenciesSpinner() {
		
		
		crypto_selected = Codes.getCryptoCoinByCode(left_value)
		country_selected = Codes.getCountryCoinByCode(right_value)
		
		tv_left_panel!!.text = left_value
		tv_right_panel!!.text = right_value
		
		Glide.with(this)
				.asBitmap()
				.load(crypto_selected.imageUrl)
				.into(iv_left_panel!!)
		
		Glide.with(this)
				.asBitmap()
				.load(Codes.getCountryImage(right_value))
				.into(iv_right_panel!!)
		
	}
}