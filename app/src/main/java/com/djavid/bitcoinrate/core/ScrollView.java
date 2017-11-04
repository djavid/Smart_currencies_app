package com.djavid.bitcoinrate.core;

import com.djavid.bitcoinrate.model.realm.TickerItemRealm;

import io.realm.RealmResults;


public interface ScrollView<T> extends View{
    void scrollToPosition(int position);
    void addView(T item);
    void resetFeed();
    void refreshFeed(RealmResults<TickerItemRealm> tickerItemRealms);
}
