package com.djavid.bitcoinrate.core;


public interface View {
    void showProgressbar();
    void hideProgressbar();
    void showError(int errorId);
    void dispose();
}
