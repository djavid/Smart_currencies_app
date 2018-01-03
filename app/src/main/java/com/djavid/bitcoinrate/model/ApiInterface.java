package com.djavid.bitcoinrate.model;

import com.djavid.bitcoinrate.model.dto.blockchain.BlockchainModel;
import com.djavid.bitcoinrate.model.dto.coinmarketcap.CoinMarketCapTicker;
import com.djavid.bitcoinrate.model.dto.cryptonator.CryptonatorTicker;
import com.djavid.bitcoinrate.model.dto.cryptonator.CurrenciesModel;
import com.djavid.bitcoinrate.model.dto.cryptowatch.HistoryDataModel;
import com.djavid.bitcoinrate.model.dto.cryptowatch.PairsResult;
import com.djavid.bitcoinrate.model.dto.heroku.ResponseId;
import com.djavid.bitcoinrate.model.dto.heroku.ResponseTickers;
import com.djavid.bitcoinrate.model.dto.heroku.Subscribe;
import com.djavid.bitcoinrate.model.dto.heroku.Ticker;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    String HEROKU_URL = "https://bitcoinrate-server.herokuapp.com";


    //blockchain

    @GET("https://blockchain.info/ru/charts/market-price")
    Single<BlockchainModel> getChartValues(@Query("timespan") String timespan,
                                         @Query("sampled") boolean sampled,
                                         @Query("format") String format);


    //cryptonator

    @GET("https://api.cryptonator.com/api/full/{curr1}-{curr2}")
    Single<CryptonatorTicker> getRate(@Path("curr1") String curr1, @Path("curr2") String curr2);

    @GET("https://www.cryptonator.com/api/currencies")
    Single<CurrenciesModel> getCurrencies();


    //coinmarketcap

    @GET("https://api.coinmarketcap.com/v1/ticker/{crypto_id}")
    Single<List<CoinMarketCapTicker>> getRateCMC(@Path("crypto_id") String crypto_id, @Query("convert") String country_id);


    //cryptowatch

    @GET("https://api.cryptowat.ch/markets/{market}/{curr}/ohlc")
    Single<HistoryDataModel> getHistory(@Path("market") String market, @Path("curr") String pair,
                                              @Query("periods") int periods,
                                              @Query("after") long after);

    @GET("https://api.cryptowat.ch/pairs/{pair}")
    Single<PairsResult> getCryptowatchMarkets(@Path("pair") String pair);


    //heroku server

    @GET(HEROKU_URL + "/registerToken")
    Single<ResponseId> registerToken(@Query("token") String token, @Query("id") long id);



    @POST(HEROKU_URL + "/subscribe")
    Single<ResponseId> sendSubscribe(@Body Subscribe subscribe);

    @GET(HEROKU_URL + "/getSubscribes")
    Single<List<Subscribe>> getSubscribesByTokenId(@Header("Token") String token,
                                                   @Query("token_id") long token_id);

    @GET(HEROKU_URL + "/deleteSubscribe")
    Completable deleteSubscribe(@Header("Token") String token, @Query("id") long id);



    @POST(HEROKU_URL + "/addTicker")
    Single<ResponseId> sendTicker(@Body Ticker ticker);

    @GET(HEROKU_URL + "/getTickers")
    Single<ResponseTickers> getTickersByTokenId(@Header("Token") String token,
                                                @Query("token_id") long token_id);

    @GET(HEROKU_URL + "/getTicker")
    Single<Ticker> getTickerByTokenIdAndTickerId(@Header("Token") String token,
                                                 @Query("token_id") long token_id,
                                                 @Query("ticker_id") long ticker_id);

    @GET(HEROKU_URL + "/deleteTicker")
    Completable deleteTicker(@Header("Token") String token, @Query("id") long id);

}
