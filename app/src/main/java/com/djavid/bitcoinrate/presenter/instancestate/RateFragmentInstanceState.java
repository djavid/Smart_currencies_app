package com.djavid.bitcoinrate.presenter.instancestate;

import com.djavid.bitcoinrate.util.Codes;


public class RateFragmentInstanceState {

    private Codes.ChartOption chart_option;


    public RateFragmentInstanceState(Codes.ChartOption chart_option) {
        this.chart_option = chart_option;
    }

    public Codes.ChartOption getChart_option() {
        return chart_option;
    }
    public void setChart_option(Codes.ChartOption chart_option) {
        this.chart_option = chart_option;
    }
}
