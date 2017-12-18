package com.djavid.bitcoinrate.presenter.implementations;

import android.util.Log;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.core.BasePresenter;
import com.djavid.bitcoinrate.core.Router;
import com.djavid.bitcoinrate.model.DataRepository;
import com.djavid.bitcoinrate.model.RestDataRepository;
import com.djavid.bitcoinrate.model.dto.coinmarketcap.CoinMarketCapTicker;
import com.djavid.bitcoinrate.model.dto.heroku.Subscribe;
import com.djavid.bitcoinrate.model.dto.heroku.Ticker;
import com.djavid.bitcoinrate.presenter.interfaces.TickerFragmentPresenter;
import com.djavid.bitcoinrate.util.Codes;
import com.djavid.bitcoinrate.util.DateFormatter;
import com.djavid.bitcoinrate.util.RxUtils;
import com.djavid.bitcoinrate.view.adapter.TickerItem;
import com.djavid.bitcoinrate.view.interfaces.TickerFragmentView;

import java.net.SocketTimeoutException;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;


public class TickerFragmentPresenterImpl extends BasePresenter<TickerFragmentView, Router, Object>
        implements TickerFragmentPresenter {

    private Disposable disposable = Disposables.empty();
    private DataRepository dataRepository;
    private List<Ticker> tickers;
    private List<Subscribe> subscribes;


    @Override
    public String getId() {
        return "ticker_fragment";
    }

    @Override
    public void onStart() { }

    @Override
    public void onStop() {
        disposable.dispose();
    }

    @Override
    public void saveInstanceState(Object instanceState) {
        setInstanceState(instanceState);
    }

    public TickerFragmentPresenterImpl() {
        dataRepository = new RestDataRepository();
    }


    @Override
    public void getAllTickers() {
        setRefreshing(true);



        long token_id = App.getAppInstance().getSharedPreferences().getLong("token_id", 0);
        disposable = dataRepository.getTickersByTokenId(token_id)
                .doOnError(Throwable::printStackTrace)
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L)
                .subscribe(tickerList -> {
                    tickers = tickerList;
                    getAllSubscribes();

                }, error -> {
                    if (error.getClass().isInstance(SocketTimeoutException.class)) {
                        System.out.println("Server problem");
                    }
                    setRefreshing(false);
                });
    }


    private void getAllSubscribes() {
        setRefreshing(true);

        long token_id = App.getAppInstance().getSharedPreferences().getLong("token_id", 0);
        disposable = dataRepository.getSubscribesByTokenId(token_id)
                .doOnError(Throwable::printStackTrace)
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L)
                .subscribe(subscribeList -> {
                    subscribes = subscribeList;

                    if (getView() != null) {
                        getView().addAllTickers(tickers, subscribes);
                    }

                    setRefreshing(false);
                }, error -> {
                    setRefreshing(false);
                });
    }

    @Override
    public void deleteTicker(long ticker_id) {
        disposable = dataRepository.deleteTicker(ticker_id)
                .doOnError(Throwable::printStackTrace)
                .compose(RxUtils.applyCompletableSchedulers())
                .retry(2L)
                .subscribe(() -> {
                    Log.d("LabelDialog", "Successfully deleted ticker with id = " + ticker_id);
                }, error -> {

                });
    }

    @Override
    public void loadTickerPrice(TickerItem tickerItem) {
        loadTickerPriceCMC(tickerItem);
    }

    @Override
    public void loadTickerPriceCryptonator(TickerItem tickerItem) {
        setRefreshing(true);

        String curr1 = tickerItem.getTickerItem().getCryptoId();
        String curr2 = tickerItem.getTickerItem().getCountryId();

        disposable = dataRepository.getRate(curr1, curr2)
                .doOnError(Throwable::printStackTrace)
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L)
                .subscribe(ticker -> {
                    if (!ticker.getError().isEmpty()) {
                        //TODO
                        Log.e("showRate():", ticker.getError());
                        return;
                    }

                    double price = ticker.getTicker().getPrice();
                    String text = DateFormatter.convertPrice(price);

                    tickerItem.setPrice(text);
                    setRefreshing(false);

                }, error -> {
                    setRefreshing(false);
                });
    }

    @Override
    public void loadTickerPriceCMC(TickerItem tickerItem) {
        setRefreshing(true);

        String code_crypto = tickerItem.getTickerItem().getCryptoId();
        final String code_crypto_full = Codes.getCryptoCurrencyId(code_crypto);
        String code_country = tickerItem.getTickerItem().getCountryId();

        disposable = dataRepository.getRateCMC(code_crypto_full, code_country)
                .doOnError(Throwable::printStackTrace)
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L)
                .subscribe(array -> {
                    CoinMarketCapTicker ticker = array.get(0);

                    double price = ticker.getPrice(code_country);
                    String text = DateFormatter.convertPrice(price);

                    tickerItem.setPrice(text);
                    setRefreshing(false);

                }, error -> {
                    setRefreshing(false);
                });
    }

    private void setRefreshing(boolean key) {
        if (getView() != null) {
            if (getView().getRefreshLayout() != null)
                getView().getRefreshLayout().setRefreshing(key);
        }
    }

    private void sendTokenToServer(String token) {

        long id;
        //if not found preference then is default 0
        id = App.getAppInstance().getSharedPreferences().getLong("token_id", 0);

        dataRepository.registerToken(token, id)
                .compose(RxUtils.applySingleSchedulers())
                .subscribe(response -> {

                    if (response.error.isEmpty()) {

                        if (response.id != 0) {
                            App.getAppInstance()
                                    .getSharedPreferences()
                                    .edit()
                                    .putLong("token_id", response.id)
                                    .apply();

                            App.getAppInstance()
                                    .getSharedPreferences()
                                    .edit()
                                    .putString("token", token)
                                    .apply();
                        }
                    }
                });
    }

    @Override
    public List<Ticker> getTickersLocal() {
        return tickers;
    }
}
