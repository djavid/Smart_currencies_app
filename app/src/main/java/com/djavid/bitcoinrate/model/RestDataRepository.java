package com.djavid.bitcoinrate.model;


import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.model.dto.blockchain.BlockchainModel;
import com.djavid.bitcoinrate.model.dto.coinmarketcap.CoinMarketCapTicker;
import com.djavid.bitcoinrate.model.dto.cryptonator.CryptonatorTicker;
import com.djavid.bitcoinrate.model.dto.cryptonator.CurrenciesModel;
import com.djavid.bitcoinrate.model.dto.cryptowatch.HistoryDataModel;
import com.djavid.bitcoinrate.model.dto.heroku.Subscribe;
import com.djavid.bitcoinrate.model.dto.heroku.ResponseId;
import com.djavid.bitcoinrate.model.dto.heroku.Ticker;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;


public class RestDataRepository implements DataRepository {

    private ApiInterface apiInterface;

    public RestDataRepository(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public RestDataRepository() {
        this(App.getAppInstance().getApiInterface());
    }

    @Override
    public Single<CryptonatorTicker> getRate(String curr1, String curr2) {
        return apiInterface.getRate(curr1, curr2);
    }

    @Override
    public Single<CurrenciesModel> getCurrencies() {
        return apiInterface.getCurrencies();
    }

    @Override
    public Single<BlockchainModel> getChartValues(String timespan, boolean sampled, String format) {
        return apiInterface.getChartValues(timespan, sampled, format);
    }

    @Override
    public Single<HistoryDataModel> getHistory(String curr, int periods, long after) {
        return apiInterface.getHistory(curr, periods, after);
    }

    @Override
    public Single<List<CoinMarketCapTicker>> getRateCMC(String crypto_id, String country_id) {
        return apiInterface.getRateCMC(crypto_id, country_id);
    }

    @Override
    public Single<ResponseId> registerToken(String token, long id) {
        return apiInterface.registerToken(token, id);
    }

    @Override
    public Single<ResponseId> sendSubscribe(Subscribe subscribe) {
        return apiInterface.sendSubscribe(subscribe);
    }

    @Override
    public Completable deleteSubscribe(long id) {
        return apiInterface.deleteSubscribe(id);
    }

    @Override
    public Single<ResponseId> sendTicker(Ticker ticker) {
        return apiInterface.sendTicker(ticker);
    }

    @Override
    public Single<List<Ticker>> getTickersByTokenId(long token_id) {
        return apiInterface.getTickersByTokenId(token_id);
    }

    @Override
    public Single<List<Subscribe>> getSubscribesByTokenId(long token_id) {
        return apiInterface.getSubscribesByTokenId(token_id);
    }

    @Override
    public Completable deleteTicker(long id) {
        return apiInterface.deleteTicker(id);
    }
}
