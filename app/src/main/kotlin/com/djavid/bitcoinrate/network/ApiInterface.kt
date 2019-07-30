package com.djavid.bitcoinrate.network

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

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiInterface {

    @get:GET("https://www.cryptonator.com/api/currencies")
    val currencies: Single<CurrenciesModel>


    //blockchain

    @GET("https://blockchain.info/ru/charts/market-price")
    fun getChartValues(@Query("timespan") timespan: String,
                       @Query("sampled") sampled: Boolean,
                       @Query("format") format: String): Single<BlockchainModel>


    //cryptonator

    @GET("https://api.cryptonator.com/api/full/{curr1}-{curr2}")
    fun getRate(@Path("curr1") curr1: String, @Path("curr2") curr2: String): Single<CryptonatorTicker>


    //coinmarketcap

    @GET("https://api.coinmarketcap.com/v1/ticker/{crypto_id}")
    fun getRateCMC(@Path("crypto_id") crypto_id: String, @Query("convert") country_id: String): Single<List<CoinMarketCapTicker>>


    //cryptowatch

    @GET("https://api.cryptowat.ch/markets/{market}/{curr}/ohlc")
    fun getHistory(@Path("market") market: String, @Path("curr") pair: String,
                   @Query("periods") periods: Int,
                   @Query("after") after: Long): Single<HistoryDataModel>

    @GET("https://api.cryptowat.ch/pairs/{pair}")
    fun getCryptowatchMarkets(@Path("pair") pair: String): Single<PairsResult>


    //cryptocompare

    @GET("https://min-api.cryptocompare.com/data/histominute")
    fun getHistoMinute(@Query("fsym") from_symbol: String, @Query("tsym") to_symbol: String,
                       @Query("limit") limit: Int, @Query("aggregate") periods: Int,
                       @Query("e") exchange: String): Single<HistoData>

    @GET("https://min-api.cryptocompare.com/data/histohour")
    fun getHistoHour(@Query("fsym") from_symbol: String, @Query("tsym") to_symbol: String,
                     @Query("limit") limit: Int, @Query("aggregate") periods: Int,
                     @Query("e") exchange: String): Single<HistoData>

    @GET("https://min-api.cryptocompare.com/data/histoday")
    fun getHistoDay(@Query("fsym") from_symbol: String, @Query("tsym") to_symbol: String,
                    @Query("limit") limit: Int, @Query("aggregate") periods: Int,
                    @Query("e") exchange: String): Single<HistoData>


    //heroku server

    @GET("$HEROKU_URL/registerToken")
    fun registerToken(@Query("token") token: String, @Query("id") id: Long): Single<ResponseId>


    @POST("$HEROKU_URL/subscribe")
    fun sendSubscribe(@Body subscribe: Subscribe): Single<ResponseId>

    @GET("$HEROKU_URL/getSubscribes")
    fun getSubscribesByTokenId(@Header("Token") token: String,
                               @Query("token_id") token_id: Long): Single<List<Subscribe>>

    @GET("$HEROKU_URL/deleteSubscribe")
    fun deleteSubscribe(@Header("Token") token: String, @Query("id") id: Long): Completable


    @POST("$HEROKU_URL/addTicker")
    fun sendTicker(@Body ticker: Ticker): Single<ResponseId>

    @GET("$HEROKU_URL/getTickers")
    fun getTickersByTokenId(@Header("Token") token: String,
                            @Query("token_id") token_id: Long): Single<ResponseTickers>

    @GET("$HEROKU_URL/getTicker")
    fun getTickerByTokenIdAndTickerId(@Header("Token") token: String,
                                      @Query("token_id") token_id: Long,
                                      @Query("ticker_id") ticker_id: Long): Single<Ticker>

    @GET("$HEROKU_URL/deleteTicker")
    fun deleteTicker(@Header("Token") token: String, @Query("id") id: Long): Completable

    companion object {

        val HEROKU_URL = "https://bitcoinrate-server.herokuapp.com"
    }

}
