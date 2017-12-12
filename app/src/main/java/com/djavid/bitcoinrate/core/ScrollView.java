package com.djavid.bitcoinrate.core;


public interface ScrollView<T> extends View{
    void scrollToPosition(int position);
    void addView(T item);
    void resetFeed();
}
