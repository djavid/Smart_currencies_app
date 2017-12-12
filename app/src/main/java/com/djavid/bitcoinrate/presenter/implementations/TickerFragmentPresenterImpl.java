package com.djavid.bitcoinrate.presenter.implementations;

import android.util.Log;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.adapter.TickerItem;
import com.djavid.bitcoinrate.core.BasePresenter;
import com.djavid.bitcoinrate.domain.MainRouter;
import com.djavid.bitcoinrate.interactor.TickerFragmentInteractor;
import com.djavid.bitcoinrate.interactor.TickerFragmentUseCase;
import com.djavid.bitcoinrate.model.dto.coinmarketcap.CoinMarketCapTicker;
import com.djavid.bitcoinrate.model.dto.heroku.Subscribe;
import com.djavid.bitcoinrate.model.dto.heroku.Ticker;
import com.djavid.bitcoinrate.presenter.interfaces.TickerFragmentPresenter;
import com.djavid.bitcoinrate.util.Codes;
import com.djavid.bitcoinrate.util.DateFormatter;
import com.djavid.bitcoinrate.util.RxUtils;
import com.djavid.bitcoinrate.view.interfaces.TickerFragmentView;

import java.net.SocketTimeoutException;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;


public class TickerFragmentPresenterImpl extends BasePresenter<TickerFragmentView, MainRouter, Object>
        implements TickerFragmentPresenter {

    private Disposable disposable = Disposables.empty();
    private TickerFragmentInteractor tickerFragmentInteractor;
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
        tickerFragmentInteractor = new TickerFragmentUseCase();
    }


    @Override
    public void getAllTickers() {
        setRefreshing(true);

        long token_id = App.getAppInstance().getPrefencesWrapper().sharedPreferences.getLong("token_id", 0);
        disposable = tickerFragmentInteractor.getTickersByTokenId(token_id)
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

        long token_id = App.getAppInstance().getPrefencesWrapper().sharedPreferences.getLong("token_id", 0);
        disposable = tickerFragmentInteractor.getSubscribesByTokenId(token_id)
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
        disposable = tickerFragmentInteractor.deleteTicker(ticker_id)
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

        disposable = tickerFragmentInteractor.getRate(curr1, curr2)
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L)
                .subscribe(ticker -> {
                    if (!ticker.getError().isEmpty()) {
                        //TODO
                        Log.e("showRate():", ticker.getError());
                        return;
                    }

                    double price = ticker.getTicker().getPrice();
                    String text = DateFormatter.convertPrice(price, ticker);

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

        disposable = tickerFragmentInteractor.getRateCMC(code_crypto_full, code_country)
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L)
                .subscribe(array -> {
                    CoinMarketCapTicker ticker = array.get(0);

                    double price = ticker.getPrice(code_country);
                    String text = DateFormatter.convertPrice(price, ticker);

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

    @Override
    public List<Ticker> getTickersLocal() {
        return tickers;
    }
}
