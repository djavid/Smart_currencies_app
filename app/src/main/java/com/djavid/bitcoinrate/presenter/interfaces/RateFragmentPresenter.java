package com.djavid.bitcoinrate.presenter.interfaces;

import com.djavid.bitcoinrate.core.Presenter;
import com.djavid.bitcoinrate.core.Router;
import com.djavid.bitcoinrate.presenter.instancestate.RateFragmentInstanceState;
import com.djavid.bitcoinrate.util.Codes;
import com.djavid.bitcoinrate.view.interfaces.RateFragmentView;


public interface RateFragmentPresenter extends Presenter<RateFragmentView, Router, RateFragmentInstanceState> {
    void showRate(boolean refresh);
    void showRateCryptonator(boolean refresh);
    void showRateCMC(boolean refresh);
    void refresh();
    void showChart(String crypto, String country, Codes.ChartOption chartOption, boolean refresh);
}
