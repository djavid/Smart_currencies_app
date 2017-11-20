package com.djavid.bitcoinrate.presenter.implementations;

import android.support.v4.app.Fragment;
import android.util.Log;
import com.djavid.bitcoinrate.R;

import com.djavid.bitcoinrate.adapter.TickerItem;
import com.djavid.bitcoinrate.core.BasePresenter;
import com.djavid.bitcoinrate.domain.MainRouter;
import com.djavid.bitcoinrate.interactor.TickerFragmentInteractor;
import com.djavid.bitcoinrate.interactor.TickerFragmentUseCase;
import com.djavid.bitcoinrate.model.dto.coinmarketcap.CoinMarketCapTicker;
import com.djavid.bitcoinrate.model.realm.TickerItemRealm;
import com.djavid.bitcoinrate.presenter.interfaces.TickerFragmentPresenter;
import com.djavid.bitcoinrate.util.Codes;
import com.djavid.bitcoinrate.util.DateFormatter;
import com.djavid.bitcoinrate.util.RxUtils;
import com.djavid.bitcoinrate.view.interfaces.TickerFragmentView;

import java.util.Date;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


public class TickerFragmentPresenterImpl extends BasePresenter<TickerFragmentView, MainRouter, Object>
        implements TickerFragmentPresenter {

    private Realm realm;
    private Disposable disposable = Disposables.empty();
    private TickerFragmentInteractor tickerFragmentInteractor;

    private RealmResults<TickerItemRealm> tickers;


    @Override
    public String getId() {
        return "ticker_fragment";
    }

    @Override
    public void onStart() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onStop() {
        disposable.dispose();
        realm.close();
    }

    @Override
    public void saveInstanceState(Object instanceState) {
        setInstanceState(instanceState);
    }

    public TickerFragmentPresenterImpl() {
        tickerFragmentInteractor = new TickerFragmentUseCase();
    }

    @Override
    public RealmResults<TickerItemRealm> getAllTickers() {
        tickers = realm.where(TickerItemRealm.class).findAll().sort("createdAt", Sort.ASCENDING);
        tickers.addChangeListener((RealmResults<TickerItemRealm> tickerItemRealms) -> {
            if (getView() != null) getView().refreshFeed(tickerItemRealms);
        });

        return tickers;
    }

    @Override
    public void deleteTicker(Date createdAt) {
        realm.executeTransaction(realm1 -> {
            TickerItemRealm ticker = realm1
                    .where(TickerItemRealm.class)
                    .equalTo("createdAt", createdAt).findFirst();
            if (ticker != null) ticker.deleteFromRealm();
        });
    }

    @Override
    public void loadTickerPrice(TickerItem tickerItem) {
        loadTickerPriceCMC(tickerItem);
    }

    @Override
    public void loadTickerPriceCryptonator(TickerItem tickerItem) {
        setRefreshing(true);

        String curr1 = tickerItem.getCode_crypto();
        String curr2 = tickerItem.getCode_country();

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

        String code_crypto = tickerItem.getCode_crypto();
        final String code_crypto_full = Codes.getCryptoCurrencyId(code_crypto);
        String code_country = tickerItem.getCode_country();

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

    public Realm getRealm() {
        return realm;
    }
}
