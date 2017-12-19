package com.djavid.bitcoinrate.util;

import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;


public class RateChart {

    private LineChart chart;
    private int color = App.getContext().getResources().getColor(R.color.colorChart);
    private final String TAG = this.getClass().getSimpleName();


    public RateChart(View view) {
        chart = view.findViewById(R.id.chart);
    }


    public void initialize() {
        Log.i(TAG, "initialize()");

        Description desc = new Description();
        desc.setText("");
        chart.setNoDataText(chart.getResources().getString(R.string.no_data_text));
        chart.setDescription(desc);
        chart.getLegend().setEnabled(false);
        chart.getAxisRight().setDrawLabels(false);

        //chart grid
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);

        //chart borders
        //chart.getXAxis().setDrawAxisLine(false);
        //chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisRight().setDrawAxisLine(false);

        //values typeface
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setTypeface(
                Typeface.createFromAsset(chart.getContext().getAssets(), "roboto-regular.ttf"));
        chart.getXAxis().setTextColor(chart.getContext().getResources().getColor(R.color.colorPrimaryText));
        chart.getXAxis().setTextSize(10f);

        //chart.getAxisLeft().setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        chart.getAxisLeft().setTypeface(
                Typeface.createFromAsset(chart.getContext().getAssets(), "roboto-regular.ttf"));
        chart.getAxisLeft().setTextColor(chart.getContext().getResources().getColor(R.color.colorPrimaryText));
        chart.getAxisLeft().setTextSize(11f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new DateFormatter(chart));

        chart.invalidate();
    }

    public void setData(List<Entry> entries) {
        Log.i(TAG, "setData()");

        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setColor(color);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircleHole(true);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setLineWidth(2);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
    }

    public static int getChartIntervals(int days) {
        switch (days) {
            case 30:
                return  7200;
            case 90:
                return  21600;
            case 180:
                return  43200;
            case 365:
                return  86400;
            default:
                return  86400;
        }
    }

    public LineChart getChart() {
        return chart;
    }
    public void setChart(LineChart chart) {
        this.chart = chart;
    }

}
