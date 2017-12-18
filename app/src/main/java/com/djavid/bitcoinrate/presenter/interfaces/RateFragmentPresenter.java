package com.djavid.bitcoinrate.presenter.interfaces;

import com.djavid.bitcoinrate.core.Presenter;
import com.djavid.bitcoinrate.domain.MainRouter;
import com.djavid.bitcoinrate.presenter.instancestate.RateFragmentInstanceState;
import com.djavid.bitcoinrate.view.interfaces.RateFragmentView;


public interface RateFragmentPresenter extends Presenter<RateFragmentView, MainRouter, RateFragmentInstanceState> {
    void showRate(boolean update_chart);
    void showRateCryptonator(boolean update_chart, boolean refresh);
    void showRateCMC(boolean update_chart, boolean refresh);
    void showChart(String timespan);
    void refresh();
    void getHistory(String curr, int periods, long after, boolean refresh);
//    void sendTokenToServer();
}
