package com.djavid.bitcoinrate.view.interfaces;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.djavid.bitcoinrate.core.View;
import com.djavid.bitcoinrate.util.Codes;
import com.djavid.bitcoinrate.util.RateChart;


public interface RateFragmentView extends View {
    void setCurrenciesSpinner();

    Spinner getLeftSpinner();
    Spinner getRightSpinner();
    TextView getTopPanel();
    SwipeRefreshLayout getRefreshLayout();
    RateChart getRateChart();

    void setSelectedChartOption(Codes.ChartOption chartOption);
    Codes.ChartOption getSelectedChartOption();
    TextView getSelectedChartLabelView();

    void setChartLabelSelected(android.view.View view);
    void setAllChartLabelsUnselected();
}
