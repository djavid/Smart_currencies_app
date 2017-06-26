package com.djavid.bitcoinrate;

import android.view.View;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;


public class RateChart {
    private LineChart chart;

    public LineChart getChart() {
        return chart;
    }

    public void setChart(LineChart chart) {
        this.chart = chart;
    }

    public RateChart(View view) {
        chart = (LineChart) view.findViewById(R.id.chart);
    }

    public void initialize(List<Entry> entries, int color) {
        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setColor(color);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setLineWidth(1);

        LineData lineData = new LineData(dataSet);

        chart.setData(lineData);
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.getLegend().setEnabled(false);
        chart.getAxisRight().setDrawLabels(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new DayAxisValueFormatter(chart));

        chart.invalidate();
    }
}
