package com.djavid.bitcoinrate;

import android.app.Application;

import com.djavid.bitcoinrate.API.BlockchainApi;
import com.djavid.bitcoinrate.API.CryptonatorApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class App extends Application {
    private Retrofit retrofit;
    private static CryptonatorApi cryptonatorApi;
    private static BlockchainApi blockchainApi;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.cryptonator.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        cryptonatorApi = retrofit.create(CryptonatorApi.class);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://blockchain.info/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        blockchainApi = retrofit.create(BlockchainApi.class);
    }

    public static CryptonatorApi getCryptonatorApi() {
        return cryptonatorApi;
    }

    public static BlockchainApi getBlockchainApi() {
        return blockchainApi;
    }
}
