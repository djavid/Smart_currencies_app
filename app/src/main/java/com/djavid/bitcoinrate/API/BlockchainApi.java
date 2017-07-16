package com.djavid.bitcoinrate.API;

import com.djavid.bitcoinrate.Model.BlockchainModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by djavid on 16.07.17.
 */


public interface BlockchainApi {
    @GET("ru/charts/market-price")
    Call<BlockchainModel> getChartValues(@Query("timespan") String timespan,
                                         @Query("sampled") boolean sampled,
                                         @Query("format") String format);
}
