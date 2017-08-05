package com.djavid.bitcoinrate.core;

/**
 * Created by djavid on 05.08.17.
 */


public interface View {
    void showProgressbar();
    void hideProgressbar();
    void showError(int errorId);
    void dispose();
}
