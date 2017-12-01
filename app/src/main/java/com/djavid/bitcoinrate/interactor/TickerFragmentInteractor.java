package com.djavid.bitcoinrate.interactor;

import com.djavid.bitcoinrate.model.dto.coinmarketcap.CoinMarketCapTicker;
import com.djavid.bitcoinrate.model.dto.cryptonator.CryptonatorTicker;
import com.djavid.bitcoinrate.model.dto.heroku.Subscribe;
import com.djavid.bitcoinrate.model.dto.heroku.Ticker;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;


public interface TickerFragmentInteractor {
    Single<CryptonatorTicker> getRate(String curr1, String curr2);
    Single<List<CoinMarketCapTicker>> getRateCMC(String crypto_id, String country_id);
    Single<List<Ticker>> getTickersByTokenId(long token_id);
    Single<List<Subscribe>> getSubscribesByTokenId(long token_id);
    Completable deleteTicker(long id);
}
