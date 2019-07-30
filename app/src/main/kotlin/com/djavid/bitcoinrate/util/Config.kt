package com.djavid.bitcoinrate.util

import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.app.App
import com.djavid.bitcoinrate.model.project.ChartOption
import com.djavid.bitcoinrate.model.project.Coin


object Config {

    val IMAGE_BASE_URL = "https://www.cryptocompare.com"
    val PURCHASE_PRODUCT_ID = "unlimited_notifications_subscription"
    val ALLOWED_AMOUNT = 1

    val GOOGLE_PLAY_LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCg" +
            "KCAQEAhjt5eTwDu/Rmt+KvZ2oeNmT5xY8zDZ8sVwhbLR2qNnoy8rSgNeCu2z/C8xvWFqiYbjlUoemjhggCq" +
            "uv+JNwUhscxGyUN5VQtRAvVJd6kMMpO0UlFNOhdbUnnMVaaain2Fv0TiVXRzrxxMuFR+vYo1f09BYaFzkrg" +
            "vx0QrNjaaNd9EKPDKvTcrKOctS2lIQd+u7bPSN60zsQd1VFqKQyFXrxjg1Y+kXguR85sd24GP/e+N1qPoMr" +
            "B6RLWoFPfoNQY1Iex9KfciYl2ApWF4FDXUh9m18fdVs4Mh6SK8N4cOfNCypBzW5EFAQGB4roseNCfEMw46z" +
            "9v8Ai/coUQN8ljiwIDAQAB"


    var chart_options = arrayOf(ChartOption("1day", 1, 900, 96, 15, "histominute"), ChartOption("1week", 7, 7200, 82, 2, "histohour"), ChartOption("1month", 30, 21600, 120, 6, "histohour"), ChartOption("1year", 365, 259200, 122, 3, "histoday"))

    private val country_codes = arrayOf("AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BRL", "BSD", "BTN", "BWP", "BYN", "BZD", "CAD", "CDF", "CHF", "CLP", "CNY", "COP", "CRC", "CUC", "CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD", "EGP", "ERN", "ETB", "EUR", "FJD", "FKP", "GBP", "GEL", "GGP", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", "HRK", "HTG", "HUF", "IDR", "ILS", "IMP", "INR", "IQD", "IRR", "ISK", "JEP", "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KMF", "KPW", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRO", "MUR", "MVR", "MWK", "MXN", "MYR", "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR", "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLL", "SOS", "SPL*", "SRD", "STD", "SVC", "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD", "TVD", "TWD", "TZS", "UAH", "UGX", "USD", "UYU", "UZS", "VEF", "VND", "VUV", "WST", "XAF", "XCD", "XDR", "XOF", "XPF", "YER", "ZAR", "ZMW", "ZWD")
    
    var cryptoCoins = arrayOf(

            Coin("BTC", "bitcoin", "/media/19633/btc.png"), Coin("BCH", "bitcoin-cash", "/media/1383919/bch.jpg"), Coin("XRP", "ripple", "/media/19972/ripple.png"), Coin("LTC", "litecoin", "/media/19782/litecoin-logo.png"), Coin("ETH", "ethereum", "/media/20646/eth_logo.png"), Coin("NVC", "novacoin", "/media/20713/nvc.png"), Coin("NMC", "namecoin", "/media/19830/nmc.png"), Coin("PPC", "peercoin", "/media/19864/peercoin-logo.png"), Coin("DOGE", "dogecoin", "/media/19684/doge.png"), Coin("DASH", "dash", "/media/20626/imageedit_27_4355944719.png"), Coin("XEM", "nem", "/media/20490/xem.png"), Coin("XMR", "monero", "/media/19969/xmr.png"), Coin("BTG", "bitcoin-gold", "/media/12318377/btg.png"), Coin("EOS", "eos", "/media/1383652/eos_1.png"), Coin("NEO", "neo", "/media/1383858/neo.jpg"), Coin("ETC", "ethereum-classic", "/media/20275/etc2.png"), Coin("ZEC", "zcash", "/media/351360/zec.png"), Coin("WAVES", "waves", "/media/350884/waves_1.png"), Coin("USDT", "tether", "/media/1383672/usdt.png"), Coin("NXT", "nxt", "/media/20627/nxt.png"), Coin("XVG", "verge", "/media/12318032/xvg.png"), Coin("STEEM", "steem", "/media/350907/steem.png"), Coin("XLM", "stellar", "/media/20696/str.png"), Coin("BCN", "bytecoin-bcn", "/media/12318404/bcn.png"), Coin("STRAT", "stratis", "/media/351303/stratis-logo.png"), Coin("TRX", "tron", "/media/12318089/trx.png"), Coin("ADA", "cardano", "/media/12318177/ada.png"), Coin("MIOTA", "iota", "/media/1383540/iota_logo.png"), Coin("BTS", "bitshares", "/media/20705/bts.png"), Coin("ARDR", "ardor", "/media/351736/ardr.png"), Coin("MTH", "monetha", "/media/1383976/mth.png"),

            Coin("VEN", "vechain", "/media/12318129/ven.png"), Coin("ICX", "icon", "/media/12318192/icx.png"), Coin("QTUM", "qtum", "/media/1383382/qtum.png"), Coin("LSK", "lisk", "/media/352246/lsk.png"), Coin("XRB", "raiblocks", "/media/1383674/xrb.png"), Coin("PPT", "populous", "/media/1383747/ppt.png"), Coin("OMG", "omisego", "/media/1383814/omisego.png"), Coin("BNB", "binance-coin", "/media/1383947/bnb.png"), Coin("SC", "siacoin", "/media/20726/siacon-logo.png"), Coin("ZRX", "0x", "/media/1383799/zrx.png"), Coin("SNT", "status", "/media/1383568/snt.png"), Coin("WTC", "walton", "/media/12317959/wtc.png"), Coin("REP", "augur", "/media/350815/augur-logo.png"), Coin("MKR", "maker", "/media/1382296/mkr.png"), Coin("KCS", "kucoin-shares", "/media/12318389/kcs.png"), Coin("VERI", "veritaseum", "/media/1383562/veri.png"), Coin("HSR", "hshare", "/media/12318137/hsr.png"), Coin("KMD", "komodo", "/media/351408/kmd.png"), Coin("DRGN", "dragonchain", "/media/16746490/drgn.png"), Coin("ARK", "ark", "/media/351931/ark.png"), Coin("ETN", "electroneum", "/media/14761932/electroneum.png"), Coin("RHOC", "rchain", "/media/16746544/rhoc.png"), Coin("LRC", "loopring", "/media/12318135/lrc.png"), Coin("AE", "aeternity", "/media/1383836/ae.png"), Coin("IOST", "iostoken", "/media/27010459/iost.png"), Coin("BAT", "basic-attention-token", "/media/1383370/bat.png"), Coin("DCR", "decred", "/media/1382607/decred.png"), Coin("DCN", "dentacoin", "/media/1383999/dcn.png"), Coin("DGB", "digibyte", "/media/12318264/7638-nty_400x400.jpg"), Coin("PIVX", "pivx", "/media/1382389/pivx.png"), Coin("KNC", "kyber-network", "/media/12318084/knc.png"), Coin("QASH", "qash", "/media/15887431/qash.png"), Coin("GBYTE", "byteball", "/media/351835/bytes.png"), Coin("ELF", "aelf", "/media/20780600/elf.png"), Coin("DGD", "digixdao", "/media/350851/dgd.png"), Coin("ZCL", "zclassic", "/media/351926/zcl.png"), Coin("GAS", "gas", "/media/1383858/neo.jpg"), Coin("GNT", "golem-network-tokens", "/media/351995/golem_logo.png"), Coin("FUN", "funfair", "/media/1383738/fun.png"), Coin("WAX", "wax", "/media/12318290/wax.png"), Coin("BTM", "bytom", "/media/20084/btm.png"), Coin("ETHOS", "ethos", "/media/16404851/ethos.png"), Coin("SALT", "salt", "/media/9350744/salt.jpg"), Coin("CND", "cindicator", "/media/12318283/cnd.png"), Coin("NAS", "nebulas-token", "/media/20780653/nas.png"), Coin("FCT", "factom", "/media/1382863/fct1.png"), Coin("SMART", "smartcash", "/media/1383906/smart.png"), Coin("CNX", "cryptonex", "/media/11417632/cnx.png"), Coin("AION", "aion", "/media/16746538/aion.png"), Coin("POWR", "power-ledger", "/media/12318301/powr.png"), Coin("SYS", "syscoin", "/media/25792583/sys.png"), Coin("DENT", "dent", "/media/1383613/dent.png"), Coin("NXS", "nexus", "/media/1382387/nexus.jpg"), Coin("RDD", "reddcoin", "/media/19887/rdd.png"), Coin("GXS", "gxshares", "/media/20780746/gxs.png"), Coin("MONA", "monacoin", "/media/19801/mona.png"), Coin("MAID", "maidsafecoin", "/media/352247/maid.png"), Coin("ENG", "enigma-project", "/media/12318287/eng.png"), Coin("REQ", "request-network", "/media/12318260/req.png"), Coin("BNT", "bancor", "/media/1383549/bnt.jpg"), Coin("MED", "medibloc", "/media/20046/med.png"), Coin("XZC", "zcoin", "/media/1382780/xzc1.png"), Coin("PART", "particl", "/media/1383838/part.png"), Coin("SRN", "sirin-labs-token", "/media/14913556/srn.png"), Coin("KIN", "kin", "/media/1383731/kin.png"), Coin("XP", "experience-points", "/media/12318134/xp.png"), Coin("BTX", "bitcore", "/media/1383895/btx.png"), Coin("GAME", "gamecredits", "/media/350887/game.png"), Coin("SUB", "substratum", "/media/1384011/sub1.png"), Coin("LINK", "chainlink", "/media/12318078/link.png"), Coin("QSP", "quantstamp", "/media/15887408/qsp.png"), Coin("NEBL", "neblio", "/media/1384016/nebl.png"), Coin("PAY", "tenx", "/media/1383687/pay.png"))
    
    var country_coins = arrayOf(Coin("USD",
            App.appInstance?.applicationContext?.resources?.getStringArray(R.array.country_currencies)[0]),
            Coin("EUR", App.context.resources.getStringArray(R.array.country_currencies)[1]),
            Coin("CAD", App.context.resources.getStringArray(R.array.country_currencies)[2]),
            Coin("CNY", App.context.resources.getStringArray(R.array.country_currencies)[3]),
            Coin("JPY", App.context.resources.getStringArray(R.array.country_currencies)[4]),
            Coin("PLN", App.context.resources.getStringArray(R.array.country_currencies)[5]),
            Coin("GBP", App.context.resources.getStringArray(R.array.country_currencies)[6]),
            Coin("RUB", App.context.resources.getStringArray(R.array.country_currencies)[7]),
            Coin("UAH", App.context.resources.getStringArray(R.array.country_currencies)[8]))

}
