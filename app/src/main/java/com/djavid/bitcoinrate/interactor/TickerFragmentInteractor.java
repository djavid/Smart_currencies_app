package com.djavid.bitcoinrate.interactor;

import com.djavid.bitcoinrate.model.dto.coinmarketcap.CoinMarketCapTicker;
import com.djavid.bitcoinrate.model.dto.cryptonator.CryptonatorTicker;

import java.util.List;

import io.reactivex.Single;


public interface TickerFragmentInteractor {
    Single<CryptonatorTicker> getRate(String curr1, String curr2);
    Single<List<CoinMarketCapTicker>> getRateCMC(String crypto_id, String country_id);
}
