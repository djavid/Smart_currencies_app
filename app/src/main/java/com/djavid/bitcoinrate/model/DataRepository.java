package com.djavid.bitcoinrate.model;

import com.djavid.bitcoinrate.model.dto.BlockchainModel;
import com.djavid.bitcoinrate.model.dto.CoinMarketCapTicker;
import com.djavid.bitcoinrate.model.dto.CryptonatorTicker;
import com.djavid.bitcoinrate.model.dto.CurrenciesModel;
import com.djavid.bitcoinrate.model.dto.HistoryDataModel;

import java.util.List;

import io.reactivex.Single;


public interface DataRepository {
    Single<CryptonatorTicker> getRate(String curr1, String curr2);
    Single<CurrenciesModel> getCurrencies();
    Single<BlockchainModel> getChartValues(String timespan, boolean sampled, String format);
    Single<HistoryDataModel> getHistory(String curr, int periods, long after);
    Single<List<CoinMarketCapTicker>> getRateCMC(String crypto_id, String country_id);
}
