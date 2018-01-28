package com.djavid.bitcoinrate.util;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DateFormatter implements IAxisValueFormatter {

    private BarLineChartBase<?> chart;
    private List<Long> dates;


    DateFormatter(BarLineChartBase<?> chart, List<Long> dates) {
        this.chart = chart;
        this.dates = dates;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        try {

            if (value >= dates.size() || value < 0) return "";

            Date date = new Date(dates.get((int) value) * 1000);
            String hour = new SimpleDateFormat("HH", Locale.US).format(date);
            String minute = new SimpleDateFormat("mm", Locale.US).format(date);
            int day = Integer.parseInt(new SimpleDateFormat("dd", Locale.US).format(date));
            int month = Integer.parseInt(new SimpleDateFormat("MM", Locale.US).format(date));
            int year = Integer.parseInt(new SimpleDateFormat("yy", Locale.US).format(date));

            String monthName = App.getContext().getResources().getStringArray(R.array.month_titles)[month - 1]; //TODO check
            String yearName = String.valueOf(year);

            int high_x = (int) chart.getHighestVisibleX();
            int low_x = (int) chart.getLowestVisibleX();
            if (high_x >= dates.size() || high_x < 0 || low_x >= dates.size() || low_x < 0)
                return "";
            float visible_range = Math.abs(dates.get(high_x) - dates.get(low_x));
            float days_count = visible_range / 3600 / 24;

//            System.out.println(chart.getVisibleXRange() + "-------------------------");
//            System.out.println(visible_range);
//            System.out.println(days_count);

            if (days_count >= 180) {

                return monthName + " " + yearName;

            } else if (days_count >= 2 && days_count < 180) {

                return day == 0 ? "" : day + " " + monthName;

            } else if (days_count < 2 && days_count > 0) {

                return hour + ":" + minute;

            } else {
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }



}