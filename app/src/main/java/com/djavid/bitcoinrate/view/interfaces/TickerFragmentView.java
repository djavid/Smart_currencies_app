package com.djavid.bitcoinrate.view.interfaces;

import android.support.v4.widget.SwipeRefreshLayout;

import com.djavid.bitcoinrate.core.ScrollView;
import com.djavid.bitcoinrate.model.dto.heroku.Ticker;

import com.djavid.bitcoinrate.model.dto.heroku.Subscribe;
import com.djavid.bitcoinrate.model.realm.TickerItemRealm;

import java.util.List;


public interface TickerFragmentView extends ScrollView<TickerItemRealm> {
    void showSnackbar(String message);
    SwipeRefreshLayout getRefreshLayout();
    void addAllTickers(List<Ticker> tickers, List<Subscribe> subscribes);
}
