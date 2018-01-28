package com.djavid.bitcoinrate.util;

import com.annimon.stream.Collectors;
import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.model.project.Coin;

import static com.djavid.bitcoinrate.util.Config.country_coins;
import static com.djavid.bitcoinrate.util.Config.crypto_coins;


public class Codes {

    private static String[] crypto_coins_array_titles = Stream
            .of(crypto_coins)
            .map(coin -> coin.id)
            .sorted()
            .collect(Collectors.toList())
            .toArray(new String[crypto_coins.length]);


    public static int getCryptoCoinIndex(String id) {

        return IntStream.range(0, crypto_coins_array_titles.length)
                .filter(i -> crypto_coins_array_titles[i].equals(id))
                .findFirst()
                .getAsInt();
    }

    public static Coin getCryptoCoinByCode(String code) {
        return Stream.of(crypto_coins)
                .filter(coin -> coin.symbol.equals(code) | coin.id.equals(code))
                .findFirst()
                .get();
    }

    public static Coin getCountryCoinByCode(String code) {
        return Stream.of(country_coins)
                .filter(coin -> coin.symbol.equals(code) | coin.id.equals(code))
                .findFirst()
                .get();
    }

    public static String getCryptoCoinImage(String code) {
        return Stream.of(crypto_coins)
                .filter(coin -> coin.symbol.equals(code) | coin.id.equals(code))
                .findFirst()
                .get()
                .imageUrl;
    }

    public static String getCryptoCurrencyId(String code) {

        return Stream.of(crypto_coins)
                .filter(coin -> coin.symbol.equals(code) | coin.id.equals(code))
                .findFirst()
                .get().id;
    }

    public static String getCountrySymbol(String code) {

        switch (code) {
            case "USD":
                return "\u0024";
            case "EUR":
                return "\u20AC";
            case "CAD":
                return "C\u0024";
            case "CNY":
                return "\u00A5";
            case "JPY":
                return "\u00A5";
            case "PLN":
                return "z\u0142";
            case "GBP":
                return "\u00A3";
            case "RUB":
                return "\u20BD";
            case "UAH":
                return "\u20B4";
            default:
                return "";
        }
    }

    public static int getCountryImage(String code) {
        switch (code) {
            case "RUB":
                return R.drawable.russia;
            case "USD":
                return R.drawable.usa;
            case "EUR":
                return R.drawable.eu;
            case "CAD":
                return R.drawable.canada;
            case "CNY":
                return R.drawable.china;
            case "JPY":
                return R.drawable.japan;
            case "PLN":
                return R.drawable.poland;
            case "GBP":
                return R.drawable.uk;
            case "UAH":
                return R.drawable.ukraine;

            default:
                return 0;
                
        }
    }

}
