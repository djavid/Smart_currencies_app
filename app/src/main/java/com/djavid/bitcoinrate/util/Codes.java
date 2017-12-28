package com.djavid.bitcoinrate.util;

import com.annimon.stream.Collectors;
import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;
import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.util.Arrays;


public class Codes {

    public static String[] country_codes = new String[]{
            "AED",	"AFN",	"ALL",	"AMD",	"ANG",	"AOA",	"ARS",	"AUD",	"AWG",	"AZN",
            "BAM",	"BBD",	"BDT",	"BGN",	"BHD",	"BIF",	"BMD",	"BND",	"BOB",	"BRL",
            "BSD",	"BTN",	"BWP",	"BYN",	"BZD",	"CAD",	"CDF",	"CHF",	"CLP",	"CNY",
            "COP",	"CRC",	"CUC",	"CUP",	"CVE",	"CZK",	"DJF",	"DKK",	"DOP",	"DZD",
            "EGP",	"ERN",	"ETB",	"EUR",	"FJD",	"FKP",	"GBP",	"GEL",	"GGP",	"GHS",
            "GIP",	"GMD",	"GNF",	"GTQ",	"GYD",	"HKD",	"HNL",	"HRK",	"HTG",	"HUF",
            "IDR",	"ILS",	"IMP",	"INR",	"IQD",	"IRR",	"ISK",	"JEP",	"JMD",	"JOD",
            "JPY",	"KES",	"KGS",	"KHR",	"KMF",	"KPW",	"KRW",	"KWD",	"KYD",	"KZT",
            "LAK",	"LBP",	"LKR",	"LRD",	"LSL",	"LYD",	"MAD",	"MDL",	"MGA",	"MKD",
            "MMK",	"MNT",	"MOP",	"MRO",	"MUR",	"MVR",	"MWK",	"MXN",	"MYR",	"MZN",
            "NAD",	"NGN",	"NIO",	"NOK",	"NPR",	"NZD",	"OMR",	"PAB",	"PEN",	"PGK",
            "PHP",	"PKR",	"PLN",	"PYG",	"QAR",	"RON",	"RSD",	"RUB",	"RWF",	"SAR",
            "SBD",	"SCR",	"SDG",	"SEK",	"SGD",	"SHP",	"SLL",	"SOS",	"SPL*",	"SRD",
            "STD",	"SVC",	"SYP",	"SZL",	"THB",	"TJS",	"TMT",	"TND",	"TOP",	"TRY",
            "TTD",	"TVD",	"TWD",	"TZS",	"UAH",	"UGX",	"USD",	"UYU",	"UZS",	"VEF",
            "VND",	"VUV",	"WST",	"XAF",	"XCD",	"XDR",	"XOF",	"XPF",	"YER",	"ZAR",
            "ZMW",	"ZWD"
    };

    public static Coin[] crypto_coins = {
            new Coin("BTC", "bitcoin"), new Coin("BCH", "bitcoin-cash"), new Coin("XRP", "ripple"),
            new Coin("LTC", "litecoin"), new Coin("ETH", "ethereum"), new Coin("NVC", "novacoin"),
            new Coin("NMC", "namecoin"), new Coin("PPC", "peercoin"), new Coin("DOGE", "dogecoin"),
            new Coin("DASH", "dash"), new Coin("XEM", "nem"), new Coin("XMR", "monero"),
            new Coin("BTG", "bitcoin-gold"), new Coin("EOS", "eos"), new Coin("NEO", "neo"),
            new Coin("ETC", "ethereum-classic"), new Coin("ZEC", "zcash"), new Coin("WAVES", "waves"),
            new Coin("USDT", "tether"), new Coin("NXT", "nxt"), new Coin("XVG", "verge"),
            new Coin("STEEM", "steem"), new Coin("XLM", "stellar"), new Coin("BCN", "bytecoin-bcn"),
            new Coin("STRAT", "stratis"), new Coin("TRX", "tron"), new Coin("ADA", "cardano"),
            new Coin("MIOTA", "iota"), new Coin("BTS", "bitshares"), new Coin("ARDR", "ardor")
    };

    public static ChartOption[] chart_options = {
            new ChartOption("1day", 1, 900),
            new ChartOption("1week", 7, 3600),
            new ChartOption("1month", 30, 7200),
            new ChartOption("1year", 365, 86400)
    };

    public static String[] crypto_coins_array_code = Stream
            .of(crypto_coins)
            .map(coin -> coin.symbol)
            .sorted()
            .collect(Collectors.toList())
            .toArray(new String[crypto_coins.length]);

    public static String[] crypto_coins_array_titles = Stream
            .of(crypto_coins)
            .map(coin -> coin.id)
            .sorted()
            .collect(Collectors.toList())
            .toArray(new String[crypto_coins.length]);

    public static String[] getCryptoCoinsArrayFormatted() {

        if (App.getAppInstance().getPreferences().getTitleFormat().equals("codes"))
            return crypto_coins_array_code;
        else
            return crypto_coins_array_titles;
    }

    public static int getSelectedCryptoCoinIndex() {

        String code = App.getAppInstance().getPreferences().getLeftSpinnerValue();
        System.out.println(code);

        if (App.getAppInstance().getPreferences().getTitleFormat().equals("codes")) {

            String found = Stream.of(crypto_coins)
                    .filter(i -> i.id.equals(code) | i.symbol.equals(code))
                    .findFirst()
                    .get()
                    .symbol;

            System.out.println(found);
            System.out.println(IntStream.range(0, crypto_coins_array_code.length)
                    .filter(i -> crypto_coins_array_code[i].equals(found))
                    .findFirst()
                    .getAsInt());
            System.out.println(Arrays.toString(crypto_coins_array_code));

            return IntStream.range(0, crypto_coins_array_code.length)
                    .filter(i -> crypto_coins_array_code[i].equals(found))
                    .findFirst()
                    .getAsInt();

        } else {

            String found = Stream.of(crypto_coins)
                    .filter(i -> i.id.equals(code) | i.symbol.equals(code))
                    .findFirst()
                    .get()
                    .id;

            System.out.println(found);
            System.out.println(IntStream.range(0, crypto_coins_array_titles.length)
                    .filter(i -> crypto_coins_array_titles[i].equals(found))
                    .findFirst()
                    .getAsInt());
            System.out.println(Arrays.toString(crypto_coins_array_titles));

            return IntStream.range(0, crypto_coins_array_titles.length)
                    .filter(i -> crypto_coins_array_titles[i].equals(found))
                    .findFirst()
                    .getAsInt();
        }
    }

    public static String getCryptoCurrencyId(String code) {

        return Stream.of(crypto_coins)
                .filter(coin -> coin.symbol.equals(code) | coin.id.equals(code))
                .findFirst()
                .get().id;
    }

    public static boolean isCryptoCurrencyId(String code) {
        return !(Stream.of(crypto_coins)
                .filter(coin -> coin.id.equals(code))
                .count() == 0);
    }

    public static String getCryptoCurrencySymbol(String code) {

        return Stream.of(crypto_coins)
                .filter(coin -> coin.symbol.equals(code) | coin.id.equals(code))
                .findFirst()
                .get().symbol;
    }

    public static String[] country_coins = {"USD", "EUR", "CAD", "CNY", "JPY", "PLN", "GBP", "RUB", "UAH"};

    public static long getChartStartDate(ChartOption chartOption) {

        //TODO think about values
        long end = LocalDateTime.now(DateTimeZone.UTC).toDateTime().getMillis() / 1000;
        long start = end - chartOption.days * 86400;

        return start;
    }

    public static int getCurrencyImage(String code) {
        
        switch (code) {
            case "RUB":
                return R.drawable.ic_russia;
            case "USD":
                return R.drawable.ic_united_states_of_america;
            case "EUR":
                return R.drawable.ic_european_union;
            case "ISK":
                return R.drawable.ic_iceland;
            case "HKD":
                return R.drawable.ic_hong_kong;
            case "TWD":
                return R.drawable.ic_taiwan;
            case "CHF":
                return R.drawable.ic_switzerland;
            case "DKK":
                return R.drawable.ic_denmark;
            case "CLP":
                return R.drawable.ic_chile;
            case "CAD":
                return R.drawable.ic_canada;
            case "INR":
                return R.drawable.ic_india;
            case "CNY":
                return R.drawable.ic_china;
            case "THB":
                return R.drawable.ic_thailand;
            case "AUD":
                return R.drawable.ic_australia;
            case "SGD":
                return R.drawable.ic_singapore;
            case "KRW":
                return R.drawable.ic_south_korea;
            case "JPY":
                return R.drawable.ic_japan;
            case "PLN":
                return R.drawable.ic_republic_of_poland;
            case "GBP":
                return R.drawable.ic_united_kingdom;
            case "SEK":
                return R.drawable.ic_sweden;
            case "NZD":
                return R.drawable.ic_new_zealand;
            case "BRL":
                return R.drawable.ic_brazil;
            case "UAH":
                return R.drawable.ic_ukraine;


            case "ARDR":
            case "ardor":
                return R.drawable.ic_ardr;

            case "BCN":
            case "bytecoin-bcn":
                return R.drawable.ic_bcn;

            case "BTS":
            case "bitshares":
                return R.drawable.ic_bts;

            case "DASH":
            case "dash":
                return R.drawable.ic_dash;

            case "ETC":
            case "ethereum-classic":
                return R.drawable.ic_etc;

            case "MIOTA":
            case "iota":
                return R.drawable.ic_iota;

            case "NXT":
            case "nxt":
                return R.drawable.ic_nxt;

            case "STEEM":
            case "steem":
                return R.drawable.ic_steem;

            case "STRAT":
            case "stratis":
                return R.drawable.ic_strat;

            case "USDT":
            case "tether":
                return R.drawable.ic_usdt;

            case "WAVES":
            case "waves":
                return R.drawable.ic_waves;

            case "XEM":
            case "nem":
                return R.drawable.ic_xem;

            case "XMR":
            case "monero":
                return R.drawable.ic_xmr;

            case "XRP":
            case "ripple":
                return R.drawable.ic_xrp;

            case "XVG":
            case "verge":
                return R.drawable.ic_xvg;

            case "ZEC":
            case "zcash":
                return R.drawable.ic_zec;

            case "BTC":
            case "bitcoin":
                return R.drawable.ic_bitcoin;

            case "BCH":
            case "bitcoin-cash":
                return R.drawable.ic_bitcoin_cash;

            case "LTC":
            case "litecoin":
                return R.drawable.ic_ltc;

            case "NMC":
            case "namecoin":
                return R.drawable.ic_namecoin;

            case "DOGE":
            case "dogecoin":
                return R.drawable.ic_dogecoin;

            case "ETH":
            case "ethereum":
                return R.drawable.ic_eth;

            case "NVC":
            case "novacoin":
                return R.drawable.ic_novacoin;

            case "PPC":
            case "peercoin":
                return R.drawable.ic_peercoin;

            case "ADA":
            case "cardano":
                return R.drawable.ic_ada;

            case "BTG":
            case "bitcoin-gold":
                return R.drawable.ic_btg;

            case "EOS":
            case "eos":
                return R.drawable.ic_eos;

            case "NEO":
            case "neo":
                return R.drawable.ic_neo;

            case "TRX":
            case "tron":
                return R.drawable.ic_trx;

            case "XLM":
            case "stellar":
                return R.drawable.ic_xlm;


            default:
                return 0;
                
        }
    }


    public static class Coin {
        public String symbol;
        public String id;

        Coin(String symbol, String id) {
            this.symbol = symbol;
            this.id = id;
        }
    }

    public static class ChartOption {
        public String timespan;
        public int days;
        public int intervals;

        public ChartOption(String timespan, int days, int intervals) {
            this.timespan = timespan;
            this.days = days;
            this.intervals = intervals;
        }
    }
}
