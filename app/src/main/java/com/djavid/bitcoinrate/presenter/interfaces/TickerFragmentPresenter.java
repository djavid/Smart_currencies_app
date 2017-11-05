package com.djavid.bitcoinrate.presenter.interfaces;

import com.djavid.bitcoinrate.adapter.TickerItem;
import com.djavid.bitcoinrate.core.Presenter;
import com.djavid.bitcoinrate.domain.MainRouter;
import com.djavid.bitcoinrate.model.realm.TickerItemRealm;
import com.djavid.bitcoinrate.view.interfaces.TickerFragmentView;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;


public interface TickerFragmentPresenter extends Presenter<TickerFragmentView, MainRouter, Object> {
    RealmResults<TickerItemRealm> getAllTickers();
    void deleteTicker(Date createdAt);
    void loadTickerPrice(TickerItem ticker);
    Realm getRealm();
}
