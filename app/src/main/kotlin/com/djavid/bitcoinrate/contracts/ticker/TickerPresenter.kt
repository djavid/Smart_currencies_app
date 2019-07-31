package com.djavid.bitcoinrate.contracts.ticker

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.annimon.stream.Collectors
import com.annimon.stream.Stream
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.app.App
import com.djavid.bitcoinrate.model.heroku.Subscribe
import com.djavid.bitcoinrate.model.heroku.Ticker
import com.djavid.bitcoinrate.model.realm.RealmSubscribeList
import com.djavid.bitcoinrate.model.realm.RealmTickerList
import com.djavid.bitcoinrate.network.RestDataRepository
import com.djavid.bitcoinrate.util.Codes
import com.djavid.bitcoinrate.util.PriceConverter
import com.djavid.bitcoinrate.view.ticker.TickerItem
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.disposables.Disposables
import io.realm.Realm
import java.util.*

class TickerPresenter(
        private val view: TickerContract.View
) : TickerContract.Presenter {

    private val TAG = this.javaClass.simpleName
    private var disposable = Disposables.empty()
    private val dataRepository: RestDataRepository
    private var tickers: MutableList<Ticker>? = null
    private var subscribes: List<Subscribe>? = null
    private val tickersFromRealm: MutableList<Ticker>
        get() {
            Log.i(TAG, "getTickersFromRealm()")

            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            val list = realm.where(RealmTickerList::class.java).findFirst()
            realm.commitTransaction()

            val res = ArrayList<Ticker>()
            if (list != null) {
                res.addAll(list.list)
            }

            return res
        }

    private val subscribesFromRealm: List<Subscribe>
        get() {
            Log.i(TAG, "getSubscribesFromRealm()")

            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            val list = realm.where(RealmSubscribeList::class.java).findFirst()
            realm.commitTransaction()

            val res = ArrayList<Subscribe>()
            if (list != null) {
                res.addAll(list.list)
            }

            return res
        }

    init {
        dataRepository = RestDataRepository()
    }

    override fun onStart() {
        Log.i(TAG, "onStart()")

        if (view != null) {

            val tickersFromRealm = tickersFromRealm
            val subscribesFromRealm = subscribesFromRealm

            if (!tickersFromRealm.isEmpty()) {

                tickers = tickersFromRealm
                subscribes = subscribesFromRealm
    
                view.addAllTickers(tickers, subscribes)

            } else {
                view.updateRecyclerVisibility()
            }
        }
    }
    
    override fun showPopupWindow() {
        view.showPopupWindow()
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == 0) {
            
            if (resultCode == Activity.RESULT_OK) {
                
                if (data.extras != null && data.extras!!.containsKey("countryId") &&
                        data.extras!!.containsKey("cryptoId") && data.extras!!.containsKey("id")) {
                    
                    val ticker_id = data.extras!!.getLong("id")
                    val token_id = App.appInstance.preferences.tokenId
                    
                    presenter!!.addTickerFromServer(token_id, ticker_id)
                }
            }
        }
    }
    
    override fun onStop() {
        disposable.dispose()
    }

    override fun saveInstanceState(instanceState: Any) {
        instanceState = instanceState
    }


    override fun addTickerFromServer(token_id: Long, ticker_id: Long) {
        Log.i(TAG, "addTickerFromServer($token_id, $ticker_id)")

        disposable = dataRepository.getTickerByTokenIdAndTickerId(token_id, ticker_id)
                .subscribe({ ticker ->

                    tickers!!.add(ticker)
                    if (view != null) view.addTickerToAdapter(ticker)

                }, { error ->
                    if (view != null)
                        view.showError(R.string.error_loading_ticker)
                })
    }

    override fun getAllTickers(refresh: Boolean) {
        Log.i(TAG, "getAllTickers($refresh)")
        if (refresh) setRefreshing(true)

        val token_id = App.appInstance!!.preferences.tokenId

        if (token_id == 0L) {
            sendTokenToServer()

        } else {
            disposable = dataRepository.getTickersByTokenId(token_id)
                    .subscribe({ response ->

                        if (response.error.isEmpty()) {

                            tickers = response.tickers
                            getAllSubscribes()

                        } else if (response.error == "No such token!") {

                            sendTokenToServer()

                        } else {

                            if (view != null)
                                view.showError(R.string.unable_to_load_from_server)
                        }

                    }, { error ->
                        if (view != null)
                            view.showError(R.string.unable_to_load_from_server)
                        sendTokenToServer() //todo
                        setRefreshing(false)
                    })
        }
    }

    private fun getAllSubscribes() {
        Log.i(TAG, "getAllSubscribes()")

        val token_id = App.appInstance!!.preferences.tokenId

        disposable = dataRepository.getSubscribesByTokenId(token_id)
                .subscribe({ subscribeList ->

                    subscribes = subscribeList

                    val tickerListRealm = tickersFromRealm
                    val subscribeListRealm = subscribesFromRealm

                    if (App.appInstance!!.preferences.subscribesAmount != subscribes!!.size)
                        App.appInstance!!.preferences.subscribesAmount = subscribes!!.size

                    if (tickerListRealm != tickers || subscribeListRealm != subscribes) {
                        Log.i(TAG, "tickers and subscribes are NOT EQUAL")
                        saveDataToRealm(tickers, subscribes)

                        if (view != null) {
                            view.addAllTickers(tickers, subscribes)
                        }
                    } else {
                        Log.i(TAG, "tickers and subscribes are EQUAL")
                    }

                    setRefreshing(false)

                }, { error -> setRefreshing(false) })
    }


    override fun loadTickerPrice(tickerItem: TickerItem) {
        loadTickerPriceCMC(tickerItem)
    }

    override fun loadTickerPriceCryptonator(tickerItem: TickerItem) {
        setRefreshing(true)

        val curr1 = tickerItem.tickerItem!!.cryptoId
        val curr2 = tickerItem.tickerItem!!.countryId

        disposable = dataRepository.getRate(curr1, curr2)
                .subscribe({ ticker ->
                    if (!ticker.error!!.isEmpty()) {
                        if (view != null) view.showError(R.string.unable_to_load_from_server)
                        Log.e(TAG, ticker.error)
                        return@dataRepository.getRate(curr1, curr2)
                                .subscribe
                    }

                    val price = PriceConverter.convertPrice(ticker.ticker!!.price)
                    tickerItem.setPrice(price)

                    setRefreshing(false)

                }, { error -> setRefreshing(false) })
    }

    override fun loadTickerPriceCMC(tickerItem: TickerItem) {
        setRefreshing(true)

        val code_crypto = tickerItem.tickerItem!!.cryptoId
        val code_crypto_full = Codes.getCryptoCurrencyId(code_crypto)
        val code_country = tickerItem.tickerItem!!.countryId

        disposable = dataRepository.getRateCMC(code_crypto_full, code_country)
                .subscribe({ array ->
                    val ticker = array[0]

                    val price = ticker.getPrice(code_country)!!
                    val text = PriceConverter.convertPrice(price)

                    tickerItem.setPrice(text)
                    setRefreshing(false)

                }, { error -> setRefreshing(false) })
    }


    private fun sendTokenToServer() {

        val token = FirebaseInstanceId.getInstance().token
        if (token == null || token.isEmpty()) return

        val id: Long
        //if not found preference then is default 0
        id = App.appInstance!!.preferences.tokenId

        dataRepository.registerToken(token, id)
                .subscribe({ response ->

                    if (response.error.isEmpty()) {

                        //success
                        if (response.id != 0L) {
                            saveToPreferences(response.id, token)
                            getAllTickers(false)
                        }

                    } else if (!response.error.isEmpty() && response.id != 0L) {

                        //device id was already registered
                        saveToPreferences(response.id, token)
                        getAllTickers(false)

                    } else {
                        //something gone wrong
                        setRefreshing(false)
                        if (view != null) view.showError(R.string.connection_error)
                    }
                }, { error -> setRefreshing(false) })
    }

    private fun saveToPreferences(token_id: Long, token: String?) {
        App.appInstance!!.preferences.tokenId = token_id
        App.appInstance!!.preferences.token = token
    }

    override fun deleteTicker(ticker_id: Long) {

        for (ticker in tickers!!)
            if (ticker.id == ticker_id) {
                tickers!!.remove(ticker)
                break
            }

        disposable = dataRepository.deleteTicker(ticker_id)
                .subscribe({ Log.i(TAG, "Successfully deleted ticker with id = $ticker_id") }, { error -> if (view != null) view.showError(R.string.error_deleting_ticker) })
    }

    private fun saveDataToRealm(tickers: List<Ticker>?, subscribes: List<Subscribe>) {
        Log.i(TAG, "saveDataToRealm()")

        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()

        realm.where(RealmTickerList::class.java).findAll().deleteAllFromRealm()
        realm.where(RealmSubscribeList::class.java).findAll().deleteAllFromRealm()

        val realmTickerList = RealmTickerList(tickers!!)
        val realmSubscribeList = RealmSubscribeList(subscribes)
        realm.copyToRealm(realmTickerList)
        realm.copyToRealm(realmSubscribeList)

        realm.commitTransaction()
    }

    private fun setRefreshing(key: Boolean) {

        if (view != null) {
            if (view.refreshLayout != null) {
                view.refreshLayout.isRefreshing = key
            }
        }
    }

    override fun sortTickers(tickers: List<Ticker>?): List<Ticker> {

        if (tickers == null) return ArrayList()

        val direction = App.appInstance!!.preferences.sortingDirection
        val parameter = App.appInstance!!.preferences.sortingParameter

        val sorted_tickers: List<Ticker>

        when (parameter) {

            "title" -> sorted_tickers = Stream.of(tickers)
                    .sorted { a, b ->
                        val a_title = Codes.getCryptoCurrencyId(a.cryptoId)
                        val b_title = Codes.getCryptoCurrencyId(b.cryptoId)

                        a_title.compareTo(b_title)
                    }
                    .collect<List<Ticker>, Any>(Collectors.toList())

            "price" -> sorted_tickers = Stream.of(tickers)
                    .sorted { a, b ->
                        java.lang.Double.compare(
                                a.ticker!!.price,
                                b.ticker!!.price)
                    }
                    .collect<List<Ticker>, Any>(Collectors.toList())

            "market_cap" -> sorted_tickers = Stream.of(tickers)
                    .sorted { a, b ->
                        java.lang.Double.compare(
                                a.ticker!!.market_cap_usd,
                                b.ticker!!.market_cap_usd)
                    }
                    .collect<List<Ticker>, Any>(Collectors.toList())

            "hour" -> sorted_tickers = Stream.of(tickers)
                    .sorted { a, b ->
                        java.lang.Double.compare(
                                a.ticker!!.percent_change_1h,
                                b.ticker!!.percent_change_1h)
                    }
                    .collect<List<Ticker>, Any>(Collectors.toList())

            "day" -> sorted_tickers = Stream.of(tickers)
                    .sorted { a, b ->
                        java.lang.Double.compare(
                                a.ticker!!.percent_change_24h,
                                b.ticker!!.percent_change_24h)
                    }
                    .collect<List<Ticker>, Any>(Collectors.toList())

            "week" -> sorted_tickers = Stream.of(tickers)
                    .sorted { a, b ->
                        java.lang.Double.compare(
                                a.ticker!!.percent_change_7d,
                                b.ticker!!.percent_change_7d)
                    }
                    .collect<List<Ticker>, Any>(Collectors.toList())

            else -> sorted_tickers = tickers
        }

        if (direction == "descending")
            Collections.reverse(sorted_tickers)

        return sorted_tickers
    }

    override fun getTickers(): List<Ticker> {
        return if (tickers == null)
            ArrayList()
        else
            tickers
    }

    override fun getSubscribes(): List<Subscribe> {
        return if (subscribes == null)
            ArrayList()
        else
            subscribes
    }
    
    override fun addAllTickers(tickers: List<Ticker>, subscribes: List<Subscribe>) {
        var tickers = tickers
        Log.i(TAG, "addAllTickers()")
        resetFeed()
        
        tickers = presenter!!.sortTickers(tickers)
        
        for (item in tickers) {
            
            val itemSubs = Stream.of(subscribes)
                    .filter { s -> s.tickerId == item.id }
                    .toList()
            
            val price = item.ticker.price
            val text = PriceConverter.convertPrice(price)
            
            val tickerItem = TickerItem(context, rv_ticker_list, item, itemSubs)
            tickerItem.setPrice(text)
            tickerItem.setPriceChange(item.ticker.getPercentChange(
                    App.appInstance.preferences.showedPriceChange))
            
            rv_ticker_list!!.addView(tickerItem)
        }
        
        updateRecyclerVisibility()
    }
    
    override fun addTickerToAdapter(ticker: Ticker) {
        Log.i(TAG, "addTickerToAdapter()")
        
        val price = ticker.ticker.price
        val text = PriceConverter.convertPrice(price)
        
        val tickerItem = TickerItem(context, rv_ticker_list, ticker)
        tickerItem.setPrice(text)
        tickerItem.setPriceChange(ticker.ticker.getPercentChange(
                App.appInstance.preferences.showedPriceChange))
        
        rv_ticker_list!!.addView(tickerItem)
        scrollToPosition(rv_ticker_list!!.allViewResolvers.size - 1)
        
        updateRecyclerVisibility()
    }
    
}
