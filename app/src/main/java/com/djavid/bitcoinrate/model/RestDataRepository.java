package com.djavid.bitcoinrate.model;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.model.dto.blockchain.BlockchainModel;
import com.djavid.bitcoinrate.model.dto.coinmarketcap.CoinMarketCapTicker;
import com.djavid.bitcoinrate.model.dto.cryptonator.CryptonatorTicker;
import com.djavid.bitcoinrate.model.dto.cryptonator.CurrenciesModel;
import com.djavid.bitcoinrate.model.dto.cryptowatch.HistoryDataModel;
import com.djavid.bitcoinrate.model.dto.cryptowatch.PairsResult;
import com.djavid.bitcoinrate.model.dto.heroku.ResponseId;
import com.djavid.bitcoinrate.model.dto.heroku.ResponseTickers;
import com.djavid.bitcoinrate.model.dto.heroku.Subscribe;
import com.djavid.bitcoinrate.model.dto.heroku.Ticker;
import com.djavid.bitcoinrate.util.RxUtils;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;


public class RestDataRepository {

    private ApiInterface apiInterface;


    private RestDataRepository(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public RestDataRepository() {
        this(App.getAppInstance().getApiInterface());
    }


    public Single<CryptonatorTicker> getRate(String curr1, String curr2) {
        return apiInterface.getRate(curr1, curr2)
                .doOnError(Throwable::printStackTrace)
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L);
    }

    public Single<CurrenciesModel> getCurrencies() {
        return apiInterface.getCurrencies();
    }

    public Single<BlockchainModel> getChartValues(String timespan, boolean sampled, String format) {
        return apiInterface.getChartValues(timespan, sampled, format)
                .doOnError(Throwable::printStackTrace);
    }

    public Single<HistoryDataModel> getHistory(String market, String curr, int periods, long after) {
        return apiInterface.getHistory(market, curr, periods, after)
                .doOnError(Throwable::printStackTrace)
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L);
    }

    public Single<PairsResult> getCryptowatchMarkets(String pair) {
        return apiInterface.getCryptowatchMarkets(pair)
                .doOnError(Throwable::printStackTrace)
                .compose(RxUtils.applySingleSchedulers());
    }

    public Single<List<CoinMarketCapTicker>> getRateCMC(String crypto_id, String country_id) {
        return apiInterface.getRateCMC(crypto_id, country_id)
                .doOnError(Throwable::printStackTrace)
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L);
    }

    public Single<ResponseId> registerToken(String token, long id) {
        return apiInterface.registerToken(token, id)
                .doOnError(Throwable::printStackTrace)
                .compose(RxUtils.applySingleSchedulers());
    }

    public Single<ResponseId> sendSubscribe(Subscribe subscribe) {
        return apiInterface.sendSubscribe(subscribe)
                .doOnError(Throwable::printStackTrace);
    }

    public Completable deleteSubscribe(long id) {

        String token = App.getAppInstance().getPreferences().getToken();

        return apiInterface.deleteSubscribe(token, id)
                .compose(RxUtils.applyCompletableSchedulers())
                .doOnError(Throwable::printStackTrace);
    }

    public Single<ResponseId> sendTicker(Ticker ticker) {
        return apiInterface.sendTicker(ticker)
                .doOnError(Throwable::printStackTrace)
                .compose(RxUtils.applySingleSchedulers());
    }

    public Single<ResponseTickers> getTickersByTokenId(long token_id) {

        String token = App.getAppInstance().getPreferences().getToken();

        return apiInterface.getTickersByTokenId(token, token_id)
                .doOnError(Throwable::printStackTrace)
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L);
    }

    public Single<Ticker> getTickerByTokenIdAndTickerId(long token_id, long ticker_id) {

        String token = App.getAppInstance().getPreferences().getToken();

        return apiInterface.getTickerByTokenIdAndTickerId(token, token_id, ticker_id)
                .doOnError(Throwable::printStackTrace)
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L);
    }

    public Single<List<Subscribe>> getSubscribesByTokenId(long token_id) {

        String token = App.getAppInstance().getPreferences().getToken();

        return apiInterface.getSubscribesByTokenId(token, token_id)
                .doOnError(Throwable::printStackTrace)
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L);
    }

    public Completable deleteTicker(long id) {

        String token = App.getAppInstance().getPreferences().getToken();

        return apiInterface.deleteTicker(token, id)
                .doOnError(Throwable::printStackTrace)
                .compose(RxUtils.applyCompletableSchedulers())
                .retry(2L);
    }
}
