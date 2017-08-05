package com.djavid.bitcoinrate.core;

/**
 * Created by djavid on 05.08.17.
 */


public interface Presenter<V extends View, R extends Router, InstanceState> {

    void onStart();
    void onStop();
    void saveInstanceState(InstanceState instanceState);

    String getId();
    V getView();
    R getRouter();

    void setView(V view);
    void setRouter(R router);

}
