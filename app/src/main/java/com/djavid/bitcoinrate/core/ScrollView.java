package com.djavid.bitcoinrate.core;

import java.util.List;


public interface ScrollView<T> extends View{
    void scrollToPosition(int position);
    void appendFeed(List<T> feed);
    void resetFeed();
}
