package com.djavid.bitcoinrate.model;

import com.djavid.bitcoinrate.model.dto.BlockchainModel;
import com.djavid.bitcoinrate.model.dto.CryptonatorTicker;
import com.djavid.bitcoinrate.model.dto.CurrenciesModel;

import io.reactivex.Single;


public interface DataRepository {
    Single<CryptonatorTicker> getRate(String curr1, String curr2);
    Single<CurrenciesModel> getCurrencies();
    Single<BlockchainModel> getChartValues(String timespan, boolean sampled, String format);
}
