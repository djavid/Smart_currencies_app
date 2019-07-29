package com.djavid.bitcoinrate.presenter.implementations

import android.util.Log
import com.djavid.bitcoinrate.App
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.core.BasePresenter
import com.djavid.bitcoinrate.core.Router
import com.djavid.bitcoinrate.model.cryptocompare.Datum
import com.djavid.bitcoinrate.model.project.ChartOption
import com.djavid.bitcoinrate.model.realm.RealmHistoryData
import com.djavid.bitcoinrate.presenter.instancestate.RateFragmentInstanceState
import com.djavid.bitcoinrate.presenter.interfaces.RateFragmentPresenter
import com.djavid.bitcoinrate.rest.RestDataRepository
import com.djavid.bitcoinrate.util.PriceConverter
import com.djavid.bitcoinrate.view.interfaces.RateFragmentView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import io.reactivex.disposables.Disposables
import io.realm.Realm
import io.realm.RealmList
import java.util.*


class RateFragmentPresenterImpl : BasePresenter<RateFragmentView, Router, RateFragmentInstanceState>(), RateFragmentPresenter {

    private val TAG = this.javaClass.simpleName
    private var disposable = Disposables.empty()
    private val dataRepository: RestDataRepository

    private val valuesFromRealm: RealmHistoryData?
        get() {
            Log.i(TAG, "getValuesFromRealm()")

            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            val realmFound = realm.where(RealmHistoryData::class.java).findFirst()
            realm.commitTransaction()

            return realmFound
        }


    override fun onStart() {

        if (view != null) {

            //restore clicked chart option
            if (instanceState != null) {
                view.selectedChartOption = instanceState.chart_option
                if (view.selectedChartLabelView != null)
                    view.setChartLabelSelected(view.selectedChartLabelView)
            }

            //restore last price
            if (!App.appInstance.preferences.price.isEmpty())
                view.topPanel.text = App.appInstance.preferences.price

            //restore history data from realm
            val realmHistoryData = valuesFromRealm
            if (realmHistoryData != null) {

                val crypto_symbol = view.crypto_selected.symbol
                val country_symbol = view.country_selected.symbol
                val pair = crypto_symbol + country_symbol
                println(pair)
                println(realmHistoryData.pair)
                val option = view.selectedChartOption

                if (realmHistoryData.pair == pair && realmHistoryData.option == option)

                    loadValuesToChart(realmHistoryData.values)
                else {
                    clearValuesFromRealm()
                    showRate(true)
                }
            }

        }
    }

    init {
        dataRepository = RestDataRepository()
    }

    override fun onStop() {
        disposable.dispose()
    }

    override fun getId(): String {
        return "rate_fragment"
    }

    override fun saveInstanceState(instanceState: RateFragmentInstanceState) {
        instanceState = instanceState
    }

    override fun refresh() {
        showRate(true)
    }

    override fun showRate(refresh: Boolean) {
        showRateCMC(refresh)
    }

    override fun showRateCryptonator(refresh: Boolean) {
        Log.i(TAG, "showRateCryptonator($refresh)")
        if (refresh) setRefreshing(true)

        val crypto_symbol = view.crypto_selected.symbol
        val country_symbol = view.country_selected.symbol

        disposable = dataRepository.getRate(crypto_symbol, country_symbol)
                .subscribe({ ticker ->

                    if (!ticker.error.isEmpty()) {
                        if (view != null)
                            view.showError(R.string.server_error)
                        Log.e(TAG, ticker.error)

                        return@dataRepository.getRate(crypto_symbol, country_symbol)
                                .subscribe
                    }

                    val price = ticker.ticker.price
                    val text = PriceConverter.convertPrice(price) + " " + ticker.ticker.target

                    if (view != null) {
                        if (view.topPanel.text != text) {
                            view.topPanel.text = text
                            App.appInstance.preferences.price = text
                        }

                        val chartOption = view.selectedChartOption
                        showChart(crypto_symbol, country_symbol, chartOption, refresh)
                    }

                }, { error ->
                    if (view != null) view.showError(R.string.connection_error)
                    setRefreshing(false)
                })
    }

    override fun showRateCMC(refresh: Boolean) {
        Log.i(TAG, "showRateCMC($refresh)")
        if (view == null) return

        if (refresh) setRefreshing(true)

        val crypto_symbol = view.crypto_selected.symbol
        val crypto_id = view.crypto_selected.id
        val country_symbol = view.country_selected.symbol

        disposable = dataRepository.getRateCMC(crypto_id, country_symbol)
                .subscribe({ array ->

                    val ticker = array[0]

                    val price = ticker.getPrice(country_symbol)!!
                    val text = PriceConverter.convertPrice(price) + " " + country_symbol

                    if (view != null) {
                        if (view.topPanel.text != text) {
                            view.topPanel.text = text
                            App.appInstance.preferences.price = text
                        }

                        val chartOption = view.selectedChartOption
                        showChart(crypto_symbol, country_symbol, chartOption, refresh)
                    }

                }, { error ->
                    //if (getView() != null) getView().showError(R.string.connection_error);
                    showRateCryptonator(refresh)
                    setRefreshing(false)
                })
    }

    override fun showChart(crypto: String, country: String, chartOption: ChartOption, refresh: Boolean) {
        Log.i(TAG, "showChart()")

        getHistory(crypto, country, chartOption)
    }

    private fun getHistory(from_symbol: String, to_symbol: String, chartOption: ChartOption) {
        Log.i(TAG, "getHistory()")

        val onSubscribe = { result ->
            if (view == null) return

            if (result == null || result!!.response == "Error" ||
                    result!!.getData() == null || result!!.getData().isEmpty()) {
                view.rateChart.chart.clear()
                setRefreshing(false)
                return
            }

            if (!valuesEqualWithRealm(result!!.getData())) {
                saveValuesToRealm(result!!.getData(), from_symbol + to_symbol, chartOption)
                loadValuesToChart(result!!.getData())
            } else {
                if (view.rateChart.chart.isEmpty)
                    loadValuesToChart(result!!.getData())
            }

            setRefreshing(false)
        }

        val onError = { error ->
            if (view == null) return

            view.rateChart.chart.clear()
            setRefreshing(false)
        }

        when (chartOption.request) {
            "histominute" ->

                disposable = dataRepository.getHistoMinute(from_symbol, to_symbol,
                        chartOption.limit, chartOption.periods).subscribe(onSubscribe, onError)

            "histohour" ->

                disposable = dataRepository.getHistoHour(from_symbol, to_symbol,
                        chartOption.limit, chartOption.periods).subscribe(onSubscribe, onError)

            "histoday" ->

                disposable = dataRepository.getHistoDay(from_symbol, to_symbol,
                        chartOption.limit, chartOption.periods).subscribe(onSubscribe, onError)
        }
    }


    private fun valuesEqualWithRealm(values: List<Datum>): Boolean {
        Log.i(TAG, "valuesEqualWithRealm()")

        val realmFound = valuesFromRealm

        if (realmFound != null) {
            val savedValues = realmFound.values

            if (values.size == savedValues.size) {
                for (i in values.indices) {
                    if (values[i] != savedValues[i]) {
                        Log.i(TAG, "Values not equal!")
                        return false
                    }
                }

                Log.i(TAG, "Values equal!")
                return true
            }
        }

        Log.i(TAG, "Values not equal!")
        return false
    }

    private fun saveValuesToRealm(values: List<Datum>, pair: String, option: ChartOption) {
        Log.i(TAG, "saveValuesToRealm()")

        val realmList = RealmList<Datum>()
        realmList.addAll(values)
        val realmHistoryData = RealmHistoryData(realmList, pair, option)

        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.where(RealmHistoryData::class.java).findAll().deleteAllFromRealm()
        realm.copyToRealm(realmHistoryData)
        realm.commitTransaction()
    }

    private fun clearValuesFromRealm() {
        Log.i(TAG, "clearValuesFromRealm()")

        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.where(RealmHistoryData::class.java).findAll().deleteAllFromRealm()
        realm.commitTransaction()
    }


    private fun loadValuesToChart(values: List<Datum>) {
        Log.i(TAG, "loadValuesToChart()")


        val lineEntries = ArrayList<Entry>()
        val candleEntries = ArrayList<CandleEntry>()
        val dates = ArrayList<Long>()

        var j = 0
        for (i in values.indices) {

            val date = values[i].time!!

            val open = values[i].open!!.toFloat()
            val close = values[i].close!!.toFloat()

            val high = values[i].high!!.toFloat()
            val low = values[i].low!!.toFloat()

            val volume = values[i].volumefrom!!.toFloat() / 10

            if (open != 0f && close != 0f && high != 0f && low != 0f) {
                dates.add(date)
                candleEntries.add(CandleEntry(j.toFloat(), high, low, open, close))
                lineEntries.add(Entry(j.toFloat(), volume))
                j++
            }
        }

        if (view != null)
            view.rateChart.setData(candleEntries, lineEntries, dates)
    }

    private fun setRefreshing(key: Boolean) {
        //        if (getView() != null)
        //            if (getView().getRefreshLayout() != null)
        //                getView().getRefreshLayout().setRefreshing(key);
    }

}
