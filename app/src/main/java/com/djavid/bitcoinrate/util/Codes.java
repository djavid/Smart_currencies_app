package com.djavid.bitcoinrate.util;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.djavid.bitcoinrate.R;


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

    public static String[] crypto_coins_array = Stream
            .of(crypto_coins)
            .map(coin -> coin.symbol)
            .sorted()
            .collect(Collectors.toList())
            .toArray(new String[crypto_coins.length]);

    public static String[] country_coins = {"USD", "EUR", "CAD", "CNY", "JPY", "PLN", "GBP", "RUB", "UAH"};

    public static String getCryptoCurrencyId(String symbol) {

        return Stream.of(crypto_coins)
                .filter(coin -> coin.symbol.equals(symbol))
                .findFirst()
                .get().id;
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


            case "BTC":
                return R.drawable.ic_bitcoin;
            case "BCH":
                return R.drawable.ic_bitcoin_cash;
            case "LTC":
                return R.drawable.ic_litecoin;
            case "NMC":
                return R.drawable.ic_namecoin;
            case "DOGE":
                return R.drawable.ic_dogecoin;
            case "ETH":
                return R.drawable.ic_ethereum;
            case "NVC":
                return R.drawable.ic_novacoin;
            case "PPC":
                return R.drawable.ic_peercoin;


            case "XRP":
                return R.drawable.ic_xrp;
            case "XVG":
                return R.drawable.ic_xvg;


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
