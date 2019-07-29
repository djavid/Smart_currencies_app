package com.djavid.bitcoinrate.presenter.interfaces

import com.djavid.bitcoinrate.core.Presenter
import com.djavid.bitcoinrate.core.Router
import com.djavid.bitcoinrate.model.project.ChartOption
import com.djavid.bitcoinrate.presenter.instancestate.RateFragmentInstanceState
import com.djavid.bitcoinrate.view.interfaces.RateFragmentView


interface RateFragmentPresenter : Presenter<RateFragmentView, Router, RateFragmentInstanceState> {
    fun showRate(refresh: Boolean)
    fun showRateCryptonator(refresh: Boolean)
    fun showRateCMC(refresh: Boolean)
    fun refresh()
    fun showChart(crypto: String, country: String, chartOption: ChartOption, refresh: Boolean)
}
