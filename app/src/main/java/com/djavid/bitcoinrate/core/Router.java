package com.djavid.bitcoinrate.core;

import com.djavid.bitcoinrate.view.adapter.TickerItem;


public interface Router {
    void goBack();
    void showCreateLabelDialog(TickerItem tickerItem);
    TickerItem getSelectedTickerItem();
}
