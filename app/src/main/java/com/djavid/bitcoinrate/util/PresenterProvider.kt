package com.djavid.bitcoinrate.util

import com.djavid.bitcoinrate.core.Presenter
import com.djavid.bitcoinrate.presenter.implementations.RateFragmentPresenterImpl
import com.djavid.bitcoinrate.presenter.implementations.TickerFragmentPresenterImpl
import java.util.*


class PresenterProvider {

    private val presenterMap: MutableMap<String, Presenter<*, *, *>>

    init {
        presenterMap = HashMap<String, Presenter>()
    }

    fun <T : Presenter<*, *, *>> getPresenter(presenterId: String, c: Class<T>): T {
        createPresenter(presenterId)
        return c.cast(presenterMap[presenterId])
    }

    private fun createPresenter(presenterId: String) {
        if (presenterMap.containsKey(presenterId)) return

        when (presenterId) {
            "rate_fragment" -> presenterMap[presenterId] = RateFragmentPresenterImpl()
            "ticker_fragment" -> presenterMap[presenterId] = TickerFragmentPresenterImpl()
        }
    }

    fun removePresenter(presenterId: String) {
        if (presenterMap.containsKey(presenterId))
            presenterMap.remove(presenterId)
    }

}
