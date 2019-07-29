package com.djavid.bitcoinrate.view.interfaces

import android.widget.TextView

import com.djavid.bitcoinrate.core.View
import com.djavid.bitcoinrate.model.project.ChartOption
import com.djavid.bitcoinrate.model.project.Coin
import com.djavid.bitcoinrate.util.RateChart


interface RateFragmentView : View {
    val topPanel: TextView
    //SwipeRefreshLayout getRefreshLayout();

    var selectedChartOption: ChartOption

    val selectedChartLabelView: TextView

    val rateChart: RateChart
    val crypto_selected: Coin
    val country_selected: Coin

    fun setCurrenciesSpinner()
    fun setChartLabelSelected(view: android.view.View)
}
