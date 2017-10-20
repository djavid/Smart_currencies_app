package com.djavid.bitcoinrate.util;


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

    public static String[] crypto_coins = {"BTC", "LTC", "ETH", "NVC", "NMC", "PPC", "DOGE"};

    public static String[] country_coins = {"USD", "EUR", "CAD", "CNY", "JPY", "PLN", "GBP", "RUB", "UAH"};

    public static String getCurrencyFullName(String code) {
        switch (code) {
            case "RUB":
                return "Ruble;Рубль";
            case "USD":
                return "Dollar;Доллар";
            case "EUR":
                return "Euro;Евро";
            case "ISK":
                return ";";
            case "HKD":
                return ";";
            case "TWD":
                return ";";
            case "CHF":
                return ";";
            case "DKK":
                return ";";
            case "CLP":
                return ";";
            case "CAD":
                return ";";
            case "INR":
                return ";";
            case "CNY":
                return ";";
            case "THB":
                return ";";
            case "AUD":
                return ";";
            case "SGD":
                return ";";
            case "KRW":
                return ";";
            case "JPY":
                return ";";
            case "PLN":
                return ";";
            case "GBP":
                return ";";
            case "SEK":
                return ";";
            case "NZD":
                return ";";
            case "BRL":
                return ";";
            case "BTC":
                return "Bitcoin;Bitcoin";
            case "LTC":
                return "Litecoin;Litecoin";
            case "NMC":
                return "Namecoin;Namecoin";
            case "DOGE":
                return "Doge;Doge";
            case "UAH":
                return ";";
            case "ETH":
                return "Ethereum;Ethereum";
            case "NVC":
                return "Novacoin;Novacoin";
            case "PPC":
                return "Pipecoin;Pipecoin";
            default:
                return ";";
        }
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
            case "BTC":
                return R.drawable.ic_bitcoin;
            case "LTC":
                return R.drawable.ic_litecoin;
            case "NMC":
                return R.drawable.ic_namecoin;
            case "DOGE":
                return R.drawable.ic_dogecoin;
            case "UAH":
                return R.drawable.ic_ukraine;
            case "ETH":
                return R.drawable.ic_ethereum;
            case "NVC":
                return R.drawable.ic_novacoin;
            case "PPC":
                return R.drawable.ic_peercoin;
            default:
                return R.drawable.ic_european_union;
                
        }
    }
}
