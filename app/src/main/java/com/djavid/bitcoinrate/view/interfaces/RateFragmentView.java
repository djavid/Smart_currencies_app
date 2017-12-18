package com.djavid.bitcoinrate.view.interfaces;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.djavid.bitcoinrate.RateChart;
import com.djavid.bitcoinrate.core.View;


public interface RateFragmentView extends View {
    void setCurrenciesSpinner();

    Spinner getLeftSpinner();
    Spinner getRightSpinner();
    TextView getTopPanel();
    SwipeRefreshLayout getRefreshLayout();
    RateChart getRateChart();
    String getSelectedTimespan();
    int getTimespanDays();
    void setChartLabelSelected(String timespan);
}
