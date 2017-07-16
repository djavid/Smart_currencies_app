package com.djavid.bitcoinrate.API;

import com.djavid.bitcoinrate.Model.BlockchainModel;
import com.djavid.bitcoinrate.Model.CryptonatorTicker;
import com.djavid.bitcoinrate.Model.CurrenciesModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by djavid on 16.07.17.
 */


public interface CryptonatorApi {
    @GET("api/full/{curr1}-{curr2}")
    Call<CryptonatorTicker> getRate(@Path("curr1") String curr1, @Path("curr2") String curr2);

    @GET("https://www.cryptonator.com/api/currencies")
    Call<CurrenciesModel> getCurrencies();
}
