package com.djavid.bitcoinrate.contracts.ticker

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.model.heroku.Subscribe
import com.djavid.bitcoinrate.model.heroku.Ticker
import com.djavid.bitcoinrate.model.realm.RealmSubscribeList
import com.djavid.bitcoinrate.model.realm.RealmTickerList
import com.djavid.bitcoinrate.network.RestDataRepository
import com.djavid.bitcoinrate.util.Codes
import com.djavid.bitcoinrate.util.PriceConverter
import com.djavid.bitcoinrate.util.SavedPreferences
import com.djavid.bitcoinrate.view.ticker.TickerItem
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.disposables.Disposables
import io.realm.Realm
import java.util.*

class TickerPresenter(
		private val view: TickerContract.View,
		private val dataRepository: RestDataRepository,
		private val preferences: SavedPreferences,
		private val codes: Codes
) : TickerContract.Presenter {
	
	private val TAG = this.javaClass.simpleName
	private var disposable = Disposables.empty()
	private var tickers: MutableList<Ticker> = emptyList<Ticker>().toMutableList()
	private var subscribes: List<Subscribe>? = null
	private val tickersFromRealm: MutableList<Ticker>
		get() {
			Log.i(TAG, "getTickersFromRealm()")
			
			Realm.getDefaultInstance().apply {
				beginTransaction()
				val realmList = where(RealmTickerList::class.java).findFirst()?.list?.toMutableList()
				commitTransaction()
				
				return realmList ?: mutableListOf()
			}
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
	
	}
	
	override fun init() {
		view.init(this)
	}
	
	private fun onStart() {
		Log.i(TAG, "onStart()")
		
		val tickersFromRealm = tickersFromRealm
		val subscribesFromRealm = subscribesFromRealm
		
		if (tickersFromRealm.isNotEmpty()) {
			tickers = tickersFromRealm
			subscribes = subscribesFromRealm
			view.addAllTickers(tickers, subscribesFromRealm)
		} else {
			view.updateRecyclerVisibility()
		}
	}
	
	override fun sortTickers(tickers: List<Ticker>): List<Ticker> {
		return emptyList()
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
					val token_id = preferences.tokenId
					
					addTickerFromServer(token_id, ticker_id)
				}
			}
		}
	}
	
	override fun onDestroy() {
		disposable.dispose()
	}
	
	override fun addTickerFromServer(token_id: Long, ticker_id: Long) {
		Log.i(TAG, "addTickerFromServer($token_id, $ticker_id)")
		
		disposable = dataRepository.getTickerByTokenIdAndTickerId(token_id, ticker_id)
				.subscribe({ ticker ->
					tickers.add(ticker)
					view.addTickerToAdapter(ticker)
				}, { error ->
					view.showError(R.string.error_loading_ticker)
				})
	}
	
	override fun getAllTickers(refresh: Boolean) {
		Log.i(TAG, "getAllTickers($refresh)")
		if (refresh) view.setRefreshing(true)
		
		val tokenId = preferences.tokenId
		
		if (tokenId == 0L) {
			sendTokenToServer()
		} else {
			disposable = dataRepository.getTickersByTokenId(tokenId)
					.subscribe({ response ->
						when {
							response.error.isEmpty() -> {
								
								tickers = response.tickers.toMutableList()
								getAllSubscribes()
								
							}
							response.error == "No such token!" -> sendTokenToServer()
							else -> view.showError(R.string.unable_to_load_from_server)
						}
					}, { error ->
						view.showError(R.string.unable_to_load_from_server)
						sendTokenToServer() //todo
						view.setRefreshing(false)
					})
		}
	}
	
	private fun getAllSubscribes() {
		Log.i(TAG, "getAllSubscribes()")
		
		val tokenId = preferences.tokenId
		
		disposable = dataRepository.getSubscribesByTokenId(tokenId)
				.subscribe({ subscribeList ->
					
					subscribes = subscribeList
					
					val tickerListRealm = tickersFromRealm
					val subscribeListRealm = subscribesFromRealm
					
					subscribes?.let {
						preferences.subscribesAmount = it.size
						
						if (tickerListRealm != tickers || subscribeListRealm != it) {
							Log.i(TAG, "tickers and subscribes are NOT EQUAL")
							saveDataToRealm(tickers, it)
							view.addAllTickers(tickers, it)
						} else {
							Log.i(TAG, "tickers and subscribes are EQUAL")
						}
					}
					
					view.setRefreshing(false)
					
				}, { error -> view.setRefreshing(false) })
	}
	
	
	override fun loadTickerPrice(tickerItem: TickerItem) {
		loadTickerPriceCMC(tickerItem)
	}
	
	override fun loadTickerPriceCryptonator(tickerItem: TickerItem) {
		view.setRefreshing(true)
		
		val curr1 = tickerItem.tickerItem?.cryptoId
		val curr2 = tickerItem.tickerItem?.countryId
		
		if (curr1 != null && curr2 != null) {
			disposable = dataRepository.getRate(curr1, curr2)
					.subscribe({ ticker ->
						ticker.error?.let {
							if (!it.isEmpty()) {
								view.showError(R.string.unable_to_load_from_server)
								Log.e(TAG, ticker.error)

//								todo add code here
//								return @dataRepository.getRate(curr1, curr2)
//										.subscribe
							}
						}
						
						
						val price = PriceConverter.convertPrice(ticker.ticker!!.price)
						tickerItem.setPrice(price)
						
						view.setRefreshing(false)
						
					}, { error -> view.setRefreshing(false) })
		}
	}
	
	override fun loadTickerPriceCMC(tickerItem: TickerItem) {
		view.setRefreshing(true)
		
		val codeCrypto = tickerItem.tickerItem?.cryptoId
		
		codeCrypto?.let {
			val codeCryptoFull = codes.getCryptoCurrencyId(codeCrypto)
			val codeCountry = tickerItem.tickerItem?.countryId
			
			if (codeCryptoFull != null && codeCountry != null) {
				disposable = dataRepository.getRateCMC(codeCryptoFull, codeCountry)
						.subscribe({ array ->
							val ticker = array[0]
							
							val price = ticker.getPrice(codeCountry)!!
							val text = PriceConverter.convertPrice(price)
							
							tickerItem.setPrice(text)
							view.setRefreshing(false)
							
						}, { error -> view.setRefreshing(false) })
			}
		}
	}
	
	
	private fun sendTokenToServer() {
		
		val token = FirebaseInstanceId.getInstance().token
		if (token == null || token.isEmpty()) return
		
		val id: Long
		//if not found preference then is default 0
		id = preferences.tokenId
		
		val disposable = dataRepository.registerToken(token, id)
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
						view.setRefreshing(false)
						view.showError(R.string.connection_error)
					}
				}, { error -> view.setRefreshing(false) })
	}
	
	private fun saveToPreferences(token_id: Long, token: String) {
		preferences.tokenId = token_id
		preferences.token = token
	}
	
	override fun deleteTicker(ticker_id: Long) {
		for (ticker in tickers)
			if (ticker.id == ticker_id) {
				tickers.remove(ticker)
				break
			}
		
		disposable = dataRepository.deleteTicker(ticker_id)
				.subscribe(
						{ Log.i(TAG, "Successfully deleted ticker with id = $ticker_id") },
						{ error -> view.showError(R.string.error_deleting_ticker) }
				)
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
	
	fun sort2Tickers(tickers: List<Ticker>?): List<Ticker> {
		tickers ?: return emptyList()
		
		val direction = preferences.sortingDirection
		val parameter = preferences.sortingParameter
		var sortedTickers: List<Ticker> = emptyList() //todo smth wrong here
		
		sortedTickers = when (parameter) {
			"title" -> sortedTickers.sortedBy { it.cryptoId?.let { id -> codes.getCryptoCurrencyId(id) } }
			"price" -> sortedTickers.sortedBy { it.ticker?.price }
			"market_cap" -> sortedTickers.sortedBy { it.ticker?.market_cap_usd }
			"hour" -> sortedTickers.sortedBy { it.ticker?.percent_change_1h }
			"day" -> sortedTickers.sortedBy { it.ticker?.percent_change_24h }
			"week" -> sortedTickers.sortedBy {
				it.ticker?.percent_change_7d
			}
			else -> tickers
		}
		
		return if (direction == "descending")
			sortedTickers.reversed()
		else sortedTickers
	}
	
}
