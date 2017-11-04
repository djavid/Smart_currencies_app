package com.djavid.bitcoinrate.presenter.implementations;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;
import com.djavid.bitcoinrate.R;

import com.djavid.bitcoinrate.adapter.TickerItem;
import com.djavid.bitcoinrate.core.BasePresenter;
import com.djavid.bitcoinrate.domain.MainRouter;
import com.djavid.bitcoinrate.interactor.TickerFragmentInteractor;
import com.djavid.bitcoinrate.interactor.TickerFragmentUseCase;
import com.djavid.bitcoinrate.model.realm.TickerItemRealm;
import com.djavid.bitcoinrate.presenter.interfaces.TickerFragmentPresenter;
import com.djavid.bitcoinrate.util.RxUtils;
import com.djavid.bitcoinrate.view.interfaces.TickerFragmentView;

import org.joda.time.LocalDateTime;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


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
        tickers = realm.where(TickerItemRealm.class).findAll();
        tickers.addChangeListener((RealmResults<TickerItemRealm> tickerItemRealms) -> {
            getView().refreshFeed(tickerItemRealms);
        });

        return tickers;
    }

    @Override
    public void deleteTicker(TickerItemRealm ticker) {
        realm.executeTransaction(realm1 -> {
            ticker.deleteFromRealm();
            if (getView() != null) getView().showSnackbar(((Fragment) getView())
                            .getResources()
                            .getString(R.string.title_cardview_removed));
        });
    }

    @Override
    public void loadTickerPrice(TickerItem tickerItem) {
        String curr1 = tickerItem.getRealm().getCode_crypto();
        String curr2 = tickerItem.getRealm().getCode_country();

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

                    DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
                    symbols.setGroupingSeparator(' ');
                    DecimalFormat formatter;

                    if (!ticker.getTicker().getBase().equals("DOGE")) {
                        formatter = new DecimalFormat("###,###.##", symbols);
                    } else {
                        formatter = new DecimalFormat("###,###.####", symbols);
                    }
                    String text = formatter.format(price);

                    tickerItem.setPrice(text);

                }, error -> {
                    if (getView() != null) getView().hideProgressbar();
                });
    }
}
