package com.djavid.bitcoinrate.util;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;

import java.util.List;


public class RateChart {

    private CandleStickChart chart;
    private int color = App.getContext().getResources().getColor(R.color.colorChart);
    private int colorRed = App.getContext().getResources().getColor(R.color.colorPriceChangeNeg);
    private int colorGreen = App.getContext().getResources().getColor(R.color.colorPriceChangePos);
    private int colorPrimary = App.getContext().getResources().getColor(R.color.colorPrimary);
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

        //XAxis xAxis = chart.getXAxis();
        //xAxis.setValueFormatter(new DateFormatter(chart));

        chart.setPinchZoom(true);
        chart.setBackgroundColor(Color.WHITE);
        chart.setHighlightPerDragEnabled(false);

        chart.invalidate();
    }

    public void setData(List<CandleEntry> entries, List<Long> dates) {
        Log.i(TAG, "setData()");

        CandleDataSet dataSet = new CandleDataSet(entries, "");

        dataSet.setDrawIcons(false);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
//        set1.setColor(Color.rgb(80, 80, 80));
        dataSet.setShadowColor(Color.DKGRAY);
        dataSet.setShadowWidth(0.7f);
        dataSet.setDecreasingColor(colorRed);
        dataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        dataSet.setIncreasingColor(colorGreen);
        dataSet.setIncreasingPaintStyle(Paint.Style.FILL);
        dataSet.setNeutralColor(colorPrimary);
        dataSet.setShowCandleBar(true);

        //dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        //dataSet.setDrawCircleHole(true);
        //dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        //dataSet.setLineWidth(2);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new DateFormatter(chart, dates));

        CandleData lineData = new CandleData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
    }

    public CandleStickChart getChart() {
        return chart;
    }
    public void setChart(CandleStickChart chart) {
        this.chart = chart;
    }

}
