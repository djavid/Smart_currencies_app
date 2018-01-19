package com.djavid.bitcoinrate.util;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;


public class RateChart {

    private CombinedChart chart;
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

    public void setData(List<CandleEntry> candleEntries, List<Entry> lineEntries, List<Long> dates) {
        Log.i(TAG, "setData()");

        CandleDataSet candleDataSet = new CandleDataSet(candleEntries, "");
        candleDataSet.setDrawIcons(false);
        candleDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        candleDataSet.setShadowColor(Color.DKGRAY);
        candleDataSet.setShadowWidth(0.7f);
        candleDataSet.setDecreasingColor(colorRed);
        candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setIncreasingColor(colorGreen);
        candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setNeutralColor(colorPrimary);
        candleDataSet.setShowCandleBar(true);
        candleDataSet.setDrawValues(false);
        CandleData candleData = new CandleData(candleDataSet);

        LineDataSet dataSet = new LineDataSet(lineEntries, "");
        dataSet.setDrawIcons(false);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setDrawCircles(false);
        dataSet.setDrawCircleHole(true);
        dataSet.setDrawFilled(true);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setLineWidth(2);
        dataSet.setDrawValues(false);
        LineData lineData = new LineData(dataSet);

        CombinedData combinedData = new CombinedData();
        //combinedData.setData(lineData);
        combinedData.setData(candleData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new DateFormatter(chart, dates));
        chart.setData(combinedData);
        chart.invalidate();
    }

    public CombinedChart getChart() {
        return chart;
    }
    public void setChart(CombinedChart chart) {
        this.chart = chart;
    }

}
