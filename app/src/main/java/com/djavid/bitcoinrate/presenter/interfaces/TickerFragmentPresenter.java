package com.djavid.bitcoinrate.presenter.interfaces;

import com.djavid.bitcoinrate.adapter.TickerItem;
import com.djavid.bitcoinrate.core.Presenter;
import com.djavid.bitcoinrate.domain.MainRouter;
import com.djavid.bitcoinrate.model.dto.heroku.Ticker;
import com.djavid.bitcoinrate.view.interfaces.TickerFragmentView;

import java.util.List;


public interface TickerFragmentPresenter extends Presenter<TickerFragmentView, MainRouter, Object> {
    void getAllTickers();
    List<Ticker> getTickersLocal();
    void deleteTicker(long ticker_id);
    void loadTickerPrice(TickerItem tickerItem);
    void loadTickerPriceCMC(TickerItem tickerItem);
    void loadTickerPriceCryptonator(TickerItem tickerItem);

}
