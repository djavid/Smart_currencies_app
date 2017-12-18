package com.djavid.bitcoinrate.util;

import com.djavid.bitcoinrate.model.dto.coinmarketcap.CoinMarketCapTicker;
import com.djavid.bitcoinrate.model.dto.cryptonator.CryptonatorTicker;
import com.djavid.bitcoinrate.model.dto.heroku.Ticker;
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

    private String[] mMonthsEng = new String[]{
            "", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private String[] mMonthsRus = new String[]{
            "", "Янв", "Фев", "Мар", "Апр", "Май", "Июн", "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"
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

        String monthName = mMonthsRus[month]; //TODO check
        String yearName = String.valueOf(year);

        if ((chart.getVisibleXRange() / 1000 / 3600 / 24) > 30 * 5) {

            return monthName + " " + yearName;
        } else {

            return day == 0 ? "" : day + " " + monthName;
        }
    }

    public static String convertPrice(double price) {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');

        DecimalFormat formatter = new DecimalFormat(getPattern(price), symbols);

        return formatter.format(price);
    }

    private static String getPattern(Double price) {

        if (price < 1) {
            return "###,###.#####";
        } else
        if (price >= 1 && price < 10) {
            return "###,###.###";
        } else
        if (price >= 10 && price < 100) {
            return "###,###.##";
        } else
        if (price >= 100 && price < 1000) {
            return "###,###.##";
        } else
        if (price >= 1000 && price < 10000) {
            return "###,###.##";
        } else
        if (price >= 10000 && price < 100000) {
            return "###,###.#";
        } else { //if (price > 100000)
            return "###,###";
        }
    }

}