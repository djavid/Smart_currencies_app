package com.djavid.bitcoinrate.view.interfaces;

import android.content.res.Resources;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;

import com.djavid.bitcoinrate.core.ScrollView;
import com.djavid.bitcoinrate.core.View;
import com.djavid.bitcoinrate.model.realm.TickerItemRealm;
import com.mindorks.placeholderview.PlaceHolderView;


public interface TickerFragmentView extends ScrollView<TickerItemRealm> {
    void showSnackbar(String message);
    SwipeRefreshLayout getRefreshLayout();
}
