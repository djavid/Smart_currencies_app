package com.djavid.bitcoinrate.model;

import com.djavid.bitcoinrate.model.dto.blockchain.BlockchainModel;
import com.djavid.bitcoinrate.model.dto.coinmarketcap.CoinMarketCapTicker;
import com.djavid.bitcoinrate.model.dto.cryptonator.CryptonatorTicker;
import com.djavid.bitcoinrate.model.dto.cryptonator.CurrenciesModel;
import com.djavid.bitcoinrate.model.dto.cryptowatch.HistoryDataModel;
import com.djavid.bitcoinrate.model.dto.cryptowatch.PairsResult;
import com.djavid.bitcoinrate.model.dto.heroku.ResponseId;
import com.djavid.bitcoinrate.model.dto.heroku.Subscribe;
import com.djavid.bitcoinrate.model.dto.heroku.Ticker;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;


public interface DataRepository {
    Single<CryptonatorTicker> getRate(String curr1, String curr2);
    Single<CurrenciesModel> getCurrencies();
    Single<BlockchainModel> getChartValues(String timespan, boolean sampled, String format);
    Single<HistoryDataModel> getHistory(String market, String curr, int periods, long after);
    Single<PairsResult> getCryptowatchMarkets(String pair);
    Single<List<CoinMarketCapTicker>> getRateCMC(String crypto_id, String country_id);
    Single<ResponseId> registerToken(String token, long id);
    Single<ResponseId> sendSubscribe(Subscribe subscribe);
    Completable deleteSubscribe(long id);
    Single<ResponseId> sendTicker(Ticker ticker);
    Single<List<Ticker>> getTickersByTokenId(long token_id);
    Single<Ticker> getTickerByTokenIdAndTickerId(long token_id, long ticker_id);
    Single<List<Subscribe>> getSubscribesByTokenId(long token_id);
    Completable deleteTicker(long id);
}
