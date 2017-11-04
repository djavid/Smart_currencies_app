package com.djavid.bitcoinrate.domain;

import com.djavid.bitcoinrate.adapter.TickerItem;
import com.djavid.bitcoinrate.core.Router;

/**
 * Created by djavid on 05.08.17.
 */


public interface MainRouter extends Router {
    void showCreateDialog();
    void showCreateLabelDialog(TickerItem tickerItem);
    TickerItem getSelectedTickerItem();
}
