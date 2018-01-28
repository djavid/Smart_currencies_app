package com.djavid.bitcoinrate.presenter.interfaces;

import com.djavid.bitcoinrate.core.Presenter;
import com.djavid.bitcoinrate.core.Router;
import com.djavid.bitcoinrate.model.heroku.Subscribe;
import com.djavid.bitcoinrate.model.heroku.Ticker;
import com.djavid.bitcoinrate.view.adapter.TickerItem;
import com.djavid.bitcoinrate.view.interfaces.TickerFragmentView;

import java.util.List;


public interface TickerFragmentPresenter extends Presenter<TickerFragmentView, Router, Object> {

    void addTickerFromServer(long token_id, long ticker_id);
    void getAllTickers(boolean refresh);
    void deleteTicker(long ticker_id);
    void loadTickerPrice(TickerItem tickerItem);
    void loadTickerPriceCMC(TickerItem tickerItem);
    void loadTickerPriceCryptonator(TickerItem tickerItem);
    List<Ticker> sortTickers(List<Ticker> tickers);
    List<Ticker> getTickers();
    List<Subscribe> getSubscribes();
}
