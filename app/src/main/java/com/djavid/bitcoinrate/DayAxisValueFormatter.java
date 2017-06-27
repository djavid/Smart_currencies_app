package com.djavid.bitcoinrate;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


class DayAxisValueFormatter implements IAxisValueFormatter
{

    private String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private BarLineChartBase<?> chart;

    DayAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        Date date = new Date((long) value);

        int day = Integer.parseInt(new SimpleDateFormat("dd", Locale.US).format(date));
        int month = Integer.parseInt(new SimpleDateFormat("MM", Locale.US).format(date));
        int year = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.US).format(date));

        String monthName = mMonths[month % mMonths.length - 1];
        String yearName = String.valueOf(year);

        if ((chart.getVisibleXRange() / 1000 / 3600 / 24) > 30 * 5) {

            return monthName + " " + yearName;
        } else {

            return day == 0 ? "" : day + " " + monthName;
        }
    }
}