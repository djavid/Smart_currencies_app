package com.djavid.bitcoinrate.presenter.instancestate;

import com.github.mikephil.charting.charts.LineChart;


public class RateFragmentInstanceState {

    private String timespan;
    private LineChart chart;


    public RateFragmentInstanceState(String timespan, LineChart chart) {
        this.timespan = timespan;
        this.chart = chart;
    }

    public String getTimespan() {
        return timespan;
    }
    public void setTimespan(String timespan) {
        this.timespan = timespan;
    }

    public LineChart getChart() {
        return chart;
    }
    public void setChart(LineChart chart) {
        this.chart = chart;
    }
}
