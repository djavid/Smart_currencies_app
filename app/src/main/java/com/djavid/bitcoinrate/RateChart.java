package com.djavid.bitcoinrate;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;

import com.djavid.bitcoinrate.util.DateFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;


public class RateChart {

    private LineChart chart;


    public RateChart(View view) {
        chart = view.findViewById(R.id.chart);
    }


    public void initialize(List<Entry> entries, int color) {
        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setColor(color);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircleHole(true);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setLineWidth(2);

        LineData lineData = new LineData(dataSet);

        chart.setData(lineData);
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


    public LineChart getChart() {
        return chart;
    }
    public void setChart(LineChart chart) {
        this.chart = chart;
    }

}
