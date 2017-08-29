package com.djavid.bitcoinrate.presenter.interfaces;

import android.widget.Spinner;

import com.djavid.bitcoinrate.core.Presenter;
import com.djavid.bitcoinrate.domain.MainRouter;
import com.djavid.bitcoinrate.view.interfaces.RateFragmentView;

/**
 * Created by djavid on 05.08.17.
 */


public interface RateFragmentPresenter extends Presenter<RateFragmentView, MainRouter, Object> {
    void showRate(boolean update_chart);
    void showChart(String timespan);
    void refresh();
}
