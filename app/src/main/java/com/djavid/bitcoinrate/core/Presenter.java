package com.djavid.bitcoinrate.core;


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
