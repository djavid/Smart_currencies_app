package com.djavid.bitcoinrate.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


public class PriceConverter {

    public static String convertMarketCap(double market_cap) {

        if (market_cap < 1000000.0) {

            return "< 1 млн";

        } else if (market_cap >= 1000000.0 && market_cap < 10000000.0) {

            double x = Math.floor(market_cap / 1000000.0 * 10) / 10;
            return Double.toString(x) + " млн";

        } else if (market_cap >= 10000000.0 && market_cap < 1000000000.0) {

            long x = Math.round(Math.floor(market_cap / 1000000.0));
            return x + " млн";

        } else if (market_cap >= 1000000000.0 && market_cap < 10000000000.0) {

            double x = Math.floor(market_cap / 1000000000.0 * 100) / 100;
            return Double.toString(x) + " млрд";

        } else if (market_cap >= 10000000000.0 && market_cap < 1000000000000.0) {

            long x = Math.round(Math.floor(market_cap / 1000000000.0));
            return x + " млрд";

        } else if (market_cap >= 1000000000000.0) {

            double x = Math.floor(market_cap / 1000000000000.0 * 100) / 100;
            return Double.toString(x) + " трлн";

        } else {
            return "";
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
            return "###,###.######";
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
