package com.djavid.bitcoinrate.util;

import com.djavid.bitcoinrate.model.dto.CryptonatorTicker;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DateFormatter implements IAxisValueFormatter
{

    private String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private BarLineChartBase<?> chart;

    public DateFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        Date date = new Date((long) value);

        int day = Integer.parseInt(new SimpleDateFormat("dd", Locale.US).format(date));
        int month = Integer.parseInt(new SimpleDateFormat("MM", Locale.US).format(date));
        int year = Integer.parseInt(new SimpleDateFormat("yy", Locale.US).format(date));

        String monthName = mMonths[month % mMonths.length - 1];
        String yearName = String.valueOf(year);

        if ((chart.getVisibleXRange() / 1000 / 3600 / 24) > 30 * 5) {

            return monthName + " " + yearName;
        } else {

            return day == 0 ? "" : day + " " + monthName;
        }
    }

    public static String convertPrice(double price, CryptonatorTicker ticker) {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter;

        if (!ticker.getTicker().getBase().equals("DOGE")) {
            formatter = new DecimalFormat("###,###.##", symbols);
        } else {
            formatter = new DecimalFormat("###,###.####", symbols);
        }

        return formatter.format(price);
    }
}