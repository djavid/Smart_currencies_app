package com.djavid.bitcoinrate.view.rate

import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.contracts.rate.RateContract
import com.djavid.bitcoinrate.util.Codes
import com.djavid.bitcoinrate.util.Config
import kotlinx.android.synthetic.main.fragment_rate.view.*

class RateView(
		private val viewRoot: View
) : RateContract.View {
	
	private lateinit var presenter: RateContract.Presenter
	
	override fun init(presenter: RateContract.Presenter) {
		this.presenter = presenter
		
		setCurrenciesSpinner()
		setToolbar()
		setOnClickListeners()
		
		getSelectedChartOption()
		setChartLabelSelected(selectedChartLabelView)
		
		val title = viewRoot.context.resources.getString(R.string.title_search_dialog_name)
		val hint = viewRoot.context.resources.getString(R.string.title_search_dialog_hint)
	}
	
	private fun setToolbar() {
		(viewRoot.context as AppCompatActivity).supportActionBar?.setTitle(R.string.app_name)
	}
	
	override fun getSelectedChartLabelView(): TextView? {
		
		return if (selectedChartOption == Config.chart_options[0])
			optionFirst
		else if (selectedChartOption == Config.chart_options[1])
			optionSecond
		else if (selectedChartOption == Config.chart_options[2])
			optionThird
		else if (selectedChartOption == Config.chart_options[3])
			optionFourth
		else
			null
	}
	
	private fun setOnClickListeners() {
		val options = listOf(viewRoot.optionFirst, viewRoot.optionSecond,
				viewRoot.optionThird, viewRoot.optionFourth)
		options.forEach { it.setOnClickListener { presenter.onChartOptionClick() } }
		
		viewRoot.left_panel.setOnClickListener { presenter.onLeftSpinnerClicked() }
		viewRoot.right_panel.setOnClickListener { presenter.onRightSpinnerClicked() }
	}
	
	override fun setChartLabelSelected(view: View?) {
		
		when (view!!.id) {
			R.id.optionFirst -> setSelectedChartOption(Config.chart_options[0])
			R.id.optionSecond -> setSelectedChartOption(Config.chart_options[1])
			R.id.optionThird -> setSelectedChartOption(Config.chart_options[2])
			R.id.optionFourth -> setSelectedChartOption(Config.chart_options[3])
		}
		
		optionFirst!!.background = resources.getDrawable(R.drawable.bg_label_unselected)
		optionSecond!!.background = resources.getDrawable(R.drawable.bg_label_unselected)
		optionThird!!.background = resources.getDrawable(R.drawable.bg_label_unselected)
		optionFourth!!.background = resources.getDrawable(R.drawable.bg_label_unselected)
		
		view.background = resources.getDrawable(R.drawable.bg_label_selected)
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