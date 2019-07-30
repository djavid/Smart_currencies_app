package com.djavid.bitcoinrate.network

import com.djavid.bitcoinrate.app.App
import com.djavid.bitcoinrate.model.blockchain.BlockchainModel
import com.djavid.bitcoinrate.model.coinmarketcap.CoinMarketCapTicker
import com.djavid.bitcoinrate.model.cryptocompare.HistoData
import com.djavid.bitcoinrate.model.cryptonator.CryptonatorTicker
import com.djavid.bitcoinrate.model.cryptonator.CurrenciesModel
import com.djavid.bitcoinrate.model.cryptowatch.HistoryDataModel
import com.djavid.bitcoinrate.model.cryptowatch.PairsResult
import com.djavid.bitcoinrate.model.heroku.ResponseId
import com.djavid.bitcoinrate.model.heroku.ResponseTickers
import com.djavid.bitcoinrate.model.heroku.Subscribe
import com.djavid.bitcoinrate.model.heroku.Ticker
import com.djavid.bitcoinrate.util.RxUtils

import io.reactivex.Completable
import io.reactivex.Single


class RestDataRepository private constructor(private val apiInterface: ApiInterface?) {

    val currencies: Single<CurrenciesModel>
        get() = apiInterface!!.currencies

    constructor() : this(App.appInstance!!.apiInterface)


    fun getRate(curr1: String, curr2: String): Single<CryptonatorTicker> {
        return apiInterface!!.getRate(curr1, curr2)
                .doOnError(Consumer<Throwable> { it.printStackTrace() })
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L)
    }

    fun getChartValues(timespan: String, sampled: Boolean, format: String): Single<BlockchainModel> {
        return apiInterface!!.getChartValues(timespan, sampled, format)
                .doOnError(Consumer<Throwable> { it.printStackTrace() })
    }

    fun getHistory(market: String, curr: String, periods: Int, after: Long): Single<HistoryDataModel> {
        return apiInterface!!.getHistory(market, curr, periods, after)
                .doOnError(Consumer<Throwable> { it.printStackTrace() })
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L)
    }

    fun getCryptowatchMarkets(pair: String): Single<PairsResult> {
        return apiInterface!!.getCryptowatchMarkets(pair)
                .doOnError(Consumer<Throwable> { it.printStackTrace() })
                .compose(RxUtils.applySingleSchedulers())
    }

    fun getRateCMC(crypto_id: String, country_id: String): Single<List<CoinMarketCapTicker>> {
        return apiInterface!!.getRateCMC(crypto_id, country_id)
                .doOnError(Consumer<Throwable> { it.printStackTrace() })
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L)
    }

    fun registerToken(token: String, id: Long): Single<ResponseId> {
        return apiInterface!!.registerToken(token, id)
                .doOnError(Consumer<Throwable> { it.printStackTrace() })
                .compose(RxUtils.applySingleSchedulers())
    }

    fun sendSubscribe(subscribe: Subscribe): Single<ResponseId> {
        return apiInterface!!.sendSubscribe(subscribe)
                .doOnError(Consumer<Throwable> { it.printStackTrace() })
    }

    fun deleteSubscribe(id: Long): Completable {

        val token = App.appInstance!!.preferences.token

        return apiInterface!!.deleteSubscribe(token, id)
                .compose(RxUtils.applyCompletableSchedulers())
                .doOnError(Consumer<Throwable> { it.printStackTrace() })
    }

    fun sendTicker(ticker: Ticker): Single<ResponseId> {
        return apiInterface!!.sendTicker(ticker)
                .doOnError(Consumer<Throwable> { it.printStackTrace() })
                .compose(RxUtils.applySingleSchedulers())
    }

    fun getTickersByTokenId(token_id: Long): Single<ResponseTickers> {

        val token = App.appInstance!!.preferences.token

        return apiInterface!!.getTickersByTokenId(token, token_id)
                .doOnError(Consumer<Throwable> { it.printStackTrace() })
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L)
    }

    fun getTickerByTokenIdAndTickerId(token_id: Long, ticker_id: Long): Single<Ticker> {

        val token = App.appInstance!!.preferences.token

        return apiInterface!!.getTickerByTokenIdAndTickerId(token, token_id, ticker_id)
                .doOnError(Consumer<Throwable> { it.printStackTrace() })
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L)
    }

    fun getSubscribesByTokenId(token_id: Long): Single<List<Subscribe>> {

        val token = App.appInstance!!.preferences.token

        return apiInterface!!.getSubscribesByTokenId(token, token_id)
                .doOnError(Consumer<Throwable> { it.printStackTrace() })
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L)
    }

    fun deleteTicker(id: Long): Completable {

        val token = App.appInstance!!.preferences.token

        return apiInterface!!.deleteTicker(token, id)
                .doOnError(Consumer<Throwable> { it.printStackTrace() })
                .compose(RxUtils.applyCompletableSchedulers())
                .retry(2L)
    }

    fun getHistoMinute(from_symbol: String, to_symbol: String, limit: Int, periods: Int): Single<HistoData> {

        val def_exchange = "CCCAGG"

        return apiInterface!!.getHistoMinute(from_symbol, to_symbol, limit, periods, def_exchange)
                .doOnError(Consumer<Throwable> { it.printStackTrace() })
                .compose(RxUtils.applySingleSchedulers())
    }

    fun getHistoHour(from_symbol: String, to_symbol: String, limit: Int, periods: Int): Single<HistoData> {

        val def_exchange = "CCCAGG"

        return apiInterface!!.getHistoHour(from_symbol, to_symbol, limit, periods, def_exchange)
                .doOnError(Consumer<Throwable> { it.printStackTrace() })
                .compose(RxUtils.applySingleSchedulers())
    }

    fun getHistoDay(from_symbol: String, to_symbol: String, limit: Int, periods: Int): Single<HistoData> {

        val def_exchange = "CCCAGG"

        return apiInterface!!.getHistoDay(from_symbol, to_symbol, limit, periods, def_exchange)
                .doOnError(Consumer<Throwable> { it.printStackTrace() })
                .compose(RxUtils.applySingleSchedulers())
    }


}
