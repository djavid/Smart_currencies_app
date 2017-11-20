package com.djavid.bitcoinrate.interactor;


import com.djavid.bitcoinrate.model.dto.blockchain.BlockchainModel;
import com.djavid.bitcoinrate.model.dto.coinmarketcap.CoinMarketCapTicker;
import com.djavid.bitcoinrate.model.dto.cryptonator.CryptonatorTicker;
import com.djavid.bitcoinrate.model.dto.cryptonator.CurrenciesModel;
import com.djavid.bitcoinrate.model.dto.cryptowatch.HistoryDataModel;
import com.djavid.bitcoinrate.model.dto.heroku.ResponseId;

import java.util.List;

import io.reactivex.Single;


public interface RateFragmentInteractor {
    Single<CryptonatorTicker> getRate(String curr1, String curr2);
    Single<List<CoinMarketCapTicker>> getRateCMC(String crypto_id, String country_id);

    Single<CurrenciesModel> getCurrencies();
    Single<BlockchainModel> getChartValues(String timespan, boolean sampled, String format);
    Single<HistoryDataModel> getHistory(String curr, int periods, long after);
    Single<ResponseId> registerToken(String token, long id);
}
