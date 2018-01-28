package com.djavid.bitcoinrate.util;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.model.project.ChartOption;
import com.djavid.bitcoinrate.model.project.Coin;


public class Config {

    public static final String IMAGE_BASE_URL = "https://www.cryptocompare.com";
    public static final String PURCHASE_PRODUCT_ID = "unlimited_notifications_subscription";
    public static final int ALLOWED_AMOUNT = 1;

    public static final String GOOGLE_PLAY_LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCg" +
            "KCAQEAhjt5eTwDu/Rmt+KvZ2oeNmT5xY8zDZ8sVwhbLR2qNnoy8rSgNeCu2z/C8xvWFqiYbjlUoemjhggCq" +
            "uv+JNwUhscxGyUN5VQtRAvVJd6kMMpO0UlFNOhdbUnnMVaaain2Fv0TiVXRzrxxMuFR+vYo1f09BYaFzkrg" +
            "vx0QrNjaaNd9EKPDKvTcrKOctS2lIQd+u7bPSN60zsQd1VFqKQyFXrxjg1Y+kXguR85sd24GP/e+N1qPoMr" +
            "B6RLWoFPfoNQY1Iex9KfciYl2ApWF4FDXUh9m18fdVs4Mh6SK8N4cOfNCypBzW5EFAQGB4roseNCfEMw46z" +
            "9v8Ai/coUQN8ljiwIDAQAB";


    public static ChartOption[] chart_options = {
            new ChartOption("1day", 1, 900, 96, 15, "histominute"),
            new ChartOption("1week", 7, 7200, 82, 2, "histohour"),
            new ChartOption("1month", 30, 21600, 120, 6, "histohour"),
            new ChartOption("1year", 365, 259200, 122, 3, "histoday")
    };

    private static String[] country_codes = new String[]{
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

            new Coin("BTC", "bitcoin", "/media/19633/btc.png"),
            new Coin("BCH", "bitcoin-cash", "/media/1383919/bch.jpg"),
            new Coin("XRP", "ripple", "/media/19972/ripple.png"),
            new Coin("LTC", "litecoin", "/media/19782/litecoin-logo.png"),
            new Coin("ETH", "ethereum", "/media/20646/eth_logo.png"),
            new Coin("NVC", "novacoin", "/media/20713/nvc.png"),
            new Coin("NMC", "namecoin", "/media/19830/nmc.png"),
            new Coin("PPC", "peercoin", "/media/19864/peercoin-logo.png"),
            new Coin("DOGE", "dogecoin", "/media/19684/doge.png"),
            new Coin("DASH", "dash", "/media/20626/imageedit_27_4355944719.png"),
            new Coin("XEM", "nem", "/media/20490/xem.png"),
            new Coin("XMR", "monero", "/media/19969/xmr.png"),
            new Coin("BTG", "bitcoin-gold", "/media/12318377/btg.png"),
            new Coin("EOS", "eos", "/media/1383652/eos_1.png"),
            new Coin("NEO", "neo", "/media/1383858/neo.jpg"),
            new Coin("ETC", "ethereum-classic", "/media/20275/etc2.png"),
            new Coin("ZEC", "zcash", "/media/351360/zec.png"),
            new Coin("WAVES", "waves", "/media/350884/waves_1.png"),
            new Coin("USDT", "tether", "/media/1383672/usdt.png"),
            new Coin("NXT", "nxt", "/media/20627/nxt.png"),
            new Coin("XVG", "verge", "/media/12318032/xvg.png"),
            new Coin("STEEM", "steem", "/media/350907/steem.png"),
            new Coin("XLM", "stellar", "/media/20696/str.png"),
            new Coin("BCN", "bytecoin-bcn", "/media/12318404/bcn.png"),
            new Coin("STRAT", "stratis", "/media/351303/stratis-logo.png"),
            new Coin("TRX", "tron", "/media/12318089/trx.png"),
            new Coin("ADA", "cardano", "/media/12318177/ada.png"),
            new Coin("MIOTA", "iota", "/media/1383540/iota_logo.png"),
            new Coin("BTS", "bitshares", "/media/20705/bts.png"),
            new Coin("ARDR", "ardor", "/media/351736/ardr.png"),
            new Coin("MTH", "monetha", "/media/1383976/mth.png"),

            new Coin("VEN", "vechain", "/media/12318129/ven.png"),
            new Coin("ICX", "icon", "/media/12318192/icx.png"),
            new Coin("QTUM", "qtum", "/media/1383382/qtum.png"),
            new Coin("LSK", "lisk", "/media/352246/lsk.png"),
            new Coin("XRB", "raiblocks", "/media/1383674/xrb.png"),
            new Coin("PPT", "populous", "/media/1383747/ppt.png"),
            new Coin("OMG", "omisego", "/media/1383814/omisego.png"),
            new Coin("BNB", "binance-coin", "/media/1383947/bnb.png"),
            new Coin("SC", "siacoin", "/media/20726/siacon-logo.png"),
            new Coin("ZRX", "0x", "/media/1383799/zrx.png"),
            new Coin("SNT", "status", "/media/1383568/snt.png"),
            new Coin("WTC", "walton", "/media/12317959/wtc.png"),
            new Coin("REP", "augur", "/media/350815/augur-logo.png"),
            new Coin("MKR", "maker", "/media/1382296/mkr.png"),
            new Coin("KCS", "kucoin-shares", "/media/12318389/kcs.png"),
            new Coin("VERI", "veritaseum", "/media/1383562/veri.png"),
            new Coin("HSR", "hshare", "/media/12318137/hsr.png"),
            new Coin("KMD", "komodo", "/media/351408/kmd.png"),
            new Coin("DRGN", "dragonchain", "/media/16746490/drgn.png"),
            new Coin("ARK", "ark", "/media/351931/ark.png"),
            new Coin("ETN", "electroneum", "/media/14761932/electroneum.png"),
            new Coin("RHOC", "rchain", "/media/16746544/rhoc.png"),
            new Coin("LRC", "loopring", "/media/12318135/lrc.png"),
            new Coin("AE", "aeternity", "/media/1383836/ae.png"),
            new Coin("IOST", "iostoken", "/media/27010459/iost.png"),
            new Coin("BAT", "basic-attention-token", "/media/1383370/bat.png"),
            new Coin("DCR", "decred", "/media/1382607/decred.png"),
            new Coin("DCN", "dentacoin", "/media/1383999/dcn.png"),
            new Coin("DGB", "digibyte", "/media/12318264/7638-nty_400x400.jpg"),
            new Coin("PIVX", "pivx", "/media/1382389/pivx.png"),
            new Coin("KNC", "kyber-network", "/media/12318084/knc.png"),
            new Coin("QASH", "qash", "/media/15887431/qash.png"),
            new Coin("GBYTE", "byteball", "/media/351835/bytes.png"),
            new Coin("ELF", "aelf", "/media/20780600/elf.png"),
            new Coin("DGD", "digixdao", "/media/350851/dgd.png"),
            new Coin("ZCL", "zclassic", "/media/351926/zcl.png"),
            new Coin("GAS", "gas", "/media/1383858/neo.jpg"),
            new Coin("GNT", "golem-network-tokens", "/media/351995/golem_logo.png"),
            new Coin("FUN", "funfair", "/media/1383738/fun.png"),
            new Coin("WAX", "wax", "/media/12318290/wax.png"),
            new Coin("BTM", "bytom", "/media/20084/btm.png"),
            new Coin("ETHOS", "ethos", "/media/16404851/ethos.png"),
            new Coin("SALT", "salt", "/media/9350744/salt.jpg"),
            new Coin("CND", "cindicator", "/media/12318283/cnd.png"),
            new Coin("NAS", "nebulas-token", "/media/20780653/nas.png"),
            new Coin("FCT", "factom", "/media/1382863/fct1.png"),
            new Coin("SMART", "smartcash", "/media/1383906/smart.png"),
            new Coin("CNX", "cryptonex", "/media/11417632/cnx.png"),
            new Coin("AION", "aion", "/media/16746538/aion.png"),
            new Coin("POWR", "power-ledger", "/media/12318301/powr.png"),
            new Coin("SYS", "syscoin", "/media/25792583/sys.png"),
            new Coin("DENT", "dent", "/media/1383613/dent.png"),
            new Coin("NXS", "nexus", "/media/1382387/nexus.jpg"),
            new Coin("RDD", "reddcoin", "/media/19887/rdd.png"),
            new Coin("GXS", "gxshares", "/media/20780746/gxs.png"),
            new Coin("MONA", "monacoin", "/media/19801/mona.png"),
            new Coin("MAID", "maidsafecoin", "/media/352247/maid.png"),
            new Coin("ENG", "enigma-project", "/media/12318287/eng.png"),
            new Coin("REQ", "request-network", "/media/12318260/req.png"),
            new Coin("BNT", "bancor", "/media/1383549/bnt.jpg"),
            new Coin("MED", "medibloc", "/media/20046/med.png"),
            new Coin("XZC", "zcoin", "/media/1382780/xzc1.png"),
            new Coin("PART", "particl", "/media/1383838/part.png"),
            new Coin("SRN", "sirin-labs-token", "/media/14913556/srn.png"),
            new Coin("KIN", "kin", "/media/1383731/kin.png"),
            new Coin("XP", "experience-points", "/media/12318134/xp.png"),
            new Coin("BTX", "bitcore", "/media/1383895/btx.png"),
            new Coin("GAME", "gamecredits", "/media/350887/game.png"),
            new Coin("SUB", "substratum", "/media/1384011/sub1.png"),
            new Coin("LINK", "chainlink", "/media/12318078/link.png"),
            new Coin("QSP", "quantstamp", "/media/15887408/qsp.png"),
            new Coin("NEBL", "neblio", "/media/1384016/nebl.png"),
            new Coin("PAY", "tenx", "/media/1383687/pay.png")

    };

    public static Coin[] country_coins = {
            new Coin("USD", App.getContext().getResources().getStringArray(R.array.country_currencies)[0]),
            new Coin("EUR", App.getContext().getResources().getStringArray(R.array.country_currencies)[1]),
            new Coin("CAD", App.getContext().getResources().getStringArray(R.array.country_currencies)[2]),
            new Coin("CNY", App.getContext().getResources().getStringArray(R.array.country_currencies)[3]),
            new Coin("JPY", App.getContext().getResources().getStringArray(R.array.country_currencies)[4]),
            new Coin("PLN", App.getContext().getResources().getStringArray(R.array.country_currencies)[5]),
            new Coin("GBP", App.getContext().getResources().getStringArray(R.array.country_currencies)[6]),
            new Coin("RUB", App.getContext().getResources().getStringArray(R.array.country_currencies)[7]),
            new Coin("UAH", App.getContext().getResources().getStringArray(R.array.country_currencies)[8])
    };

//    public static Coin[] country_coins = {
//            new Coin("USD", "dollar"),
//            new Coin("EUR", "euro"),
//            new Coin("CAD", "canadian dollar"),
//            new Coin("CNY", "yuan"),
//            new Coin("JPY", "yen"),
//            new Coin("PLN", "zloty"),
//            new Coin("GBP", "pound sterling"),
//            new Coin("RUB", "rouble"),
//            new Coin("UAH", "hryvnia")
//    };

}
