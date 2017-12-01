package com.djavid.bitcoinrate.domain;

import com.djavid.bitcoinrate.adapter.TickerItem;
import com.djavid.bitcoinrate.core.Router;


public interface MainRouter extends Router {
    void showCreateTickerDialog();
    void showCreateLabelDialog(TickerItem tickerItem);
    TickerItem getSelectedTickerItem();
}
