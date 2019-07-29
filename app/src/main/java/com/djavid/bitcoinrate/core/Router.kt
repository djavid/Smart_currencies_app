package com.djavid.bitcoinrate.core

import com.djavid.bitcoinrate.view.adapter.TickerItem


interface Router {
    val selectedTickerItem: TickerItem
    fun goBack()
    fun showCreateLabelDialog(tickerItem: TickerItem)
}
