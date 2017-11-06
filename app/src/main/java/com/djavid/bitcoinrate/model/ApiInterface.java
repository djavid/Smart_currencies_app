package com.djavid.bitcoinrate.model;

import com.djavid.bitcoinrate.model.dto.BlockchainModel;
import com.djavid.bitcoinrate.model.dto.CoinMarketCapTicker;
import com.djavid.bitcoinrate.model.dto.CryptonatorTicker;
import com.djavid.bitcoinrate.model.dto.CurrenciesModel;
import com.djavid.bitcoinrate.model.dto.HistoryDataModel;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by djavid on 05.08.17.
 */


public interface ApiInterface {

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

    @GET("https://api.cryptowat.ch/markets/gdax/{curr}/ohlc")
    Single<HistoryDataModel> getHistory(@Path("curr") String curr,
                                              @Query("periods") int periods,
                                              @Query("after") long after);

}
