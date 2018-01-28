package com.djavid.bitcoinrate.view.interfaces;

import android.widget.TextView;

import com.djavid.bitcoinrate.core.View;
import com.djavid.bitcoinrate.model.project.ChartOption;
import com.djavid.bitcoinrate.model.project.Coin;
import com.djavid.bitcoinrate.util.RateChart;


public interface RateFragmentView extends View {

    void setCurrenciesSpinner();
    TextView getTopPanel();
    //SwipeRefreshLayout getRefreshLayout();

    ChartOption getSelectedChartOption();
    void setSelectedChartOption(ChartOption chartOption);

    TextView getSelectedChartLabelView();
    void setChartLabelSelected(android.view.View view);

    RateChart getRateChart();
    Coin getCrypto_selected();
    Coin getCountry_selected();
}
