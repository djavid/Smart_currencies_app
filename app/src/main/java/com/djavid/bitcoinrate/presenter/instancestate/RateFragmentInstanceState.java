package com.djavid.bitcoinrate.presenter.instancestate;

import com.djavid.bitcoinrate.model.project.ChartOption;


public class RateFragmentInstanceState {

    private ChartOption chart_option;


    public RateFragmentInstanceState(ChartOption chart_option) {
        this.chart_option = chart_option;
    }

    public ChartOption getChart_option() {
        return chart_option;
    }
    public void setChart_option(ChartOption chart_option) {
        this.chart_option = chart_option;
    }
}
