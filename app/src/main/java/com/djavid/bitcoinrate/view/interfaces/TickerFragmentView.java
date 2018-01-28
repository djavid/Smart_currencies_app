package com.djavid.bitcoinrate.view.interfaces;

import android.support.v4.widget.SwipeRefreshLayout;

import com.djavid.bitcoinrate.core.ScrollView;
import com.djavid.bitcoinrate.model.heroku.Subscribe;
import com.djavid.bitcoinrate.model.heroku.Ticker;

import java.util.List;


public interface TickerFragmentView extends ScrollView<Ticker> {
    void showSnackbar(String message);
    SwipeRefreshLayout getRefreshLayout();
    void addTickerToAdapter(Ticker ticker);
    void addAllTickers(List<Ticker> tickers, List<Subscribe> subscribes);
    void updateRecyclerVisibility();
}
