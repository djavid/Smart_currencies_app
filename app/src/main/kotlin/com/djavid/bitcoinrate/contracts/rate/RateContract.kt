package com.djavid.bitcoinrate.contracts.rate

import android.widget.TextView
import com.djavid.bitcoinrate.model.project.ChartOption
import com.djavid.bitcoinrate.model.project.Coin
import com.djavid.bitcoinrate.view.rate.RateChartView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry

interface RateContract {
	
	interface Presenter {
		fun init()
		fun onLeftSpinnerClicked()
		fun onRightSpinnerClicked()
		fun onChartOptionClick()
		fun showRate(refresh: Boolean)
		fun showRateCryptonator(refresh: Boolean)
		fun showRateCMC(refresh: Boolean)
		fun refresh()
		fun showChart(crypto: String, country: String, chartOption: ChartOption, refresh: Boolean)
		fun onOptionsItemSelected(itemId: Int)
	}
	
	interface View {
		fun init(presenter: Presenter)
		val topPanel: TextView
		
		var selectedChartOption: ChartOption
		
		val selectedChartLabelView: TextView
		
		val rateChart: RateChartView
		val crypto_selected: Coin
		val country_selected: Coin
		
		fun setCurrenciesSpinner()
		fun setChartLabelSelected(view: android.view.View)
	}
	
	interface ChartView {
		fun init()
		fun setData(candleEntries: List<CandleEntry>, lineEntries: List<Entry>, dates: List<Long>)
	}
	
}