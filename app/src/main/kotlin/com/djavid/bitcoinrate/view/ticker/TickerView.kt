package com.djavid.bitcoinrate.view.ticker

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.annimon.stream.Stream
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.contracts.ticker.TickerContract
import com.djavid.bitcoinrate.extensions.show
import com.djavid.bitcoinrate.model.heroku.Subscribe
import com.djavid.bitcoinrate.model.heroku.Ticker
import com.djavid.bitcoinrate.util.PriceConverter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_ticker.view.*

class TickerView(
		private val viewRoot: View
) : TickerContract.View {
	
	private val tag = javaClass.simpleName
	
	companion object {
		private const val TAG_CREATE_DIALOG = "TAG_CREATE_DIALOG"
	}
	
	private lateinit var presenter: TickerContract.Presenter
	
	override fun init(presenter: TickerContract.Presenter) {
		this.presenter = presenter
		
		//toolbar.setTitle(R.string.title_cards)
	}
	
	override val refreshLayout: SwipeRefreshLayout
		get() = TODO("not implemented")
	
	override fun showSnackbar(message: String) {
		Snackbar.make(viewRoot.tickersFragment, message, Snackbar.LENGTH_SHORT).show()
	}
	
	override fun showPopupWindow() {
		val menuItemView = viewRoot.findViewById<View>(R.id.sort)
		//val tickerPopupWindow = TickerPopupWindow(menuItemView, viewRoot.context, this)
		//tickerPopupWindow.show()
		showError("No popup for now!")
	}
	
	override fun updateRecyclerVisibility() {
		viewRoot.apply {
			val isNotEmpty = rv_ticker_list.allViewResolvers.isNotEmpty()
			swipe_container.show(isNotEmpty)
			rl_no_data.show(!isNotEmpty)
		}
	}
	
	override fun scrollToPosition(position: Int) {
		viewRoot.rv_ticker_list.scrollToPosition(position)
	}
	
	fun addView(item: Ticker) {
		viewRoot.rv_ticker_list.addView(item)
	}
	
	override fun resetFeed() {
		viewRoot.rv_ticker_list.removeAllViews()
	}

//	fun setupView(view: View): View {
//		Log.i(tag, "setupView()")
//
//		val itemTouchHelper = ItemTouchHelper(simpleCallback)
//		itemTouchHelper.attachToRecyclerView(viewRoot.rv_ticker_list)
//
//		viewRoot.swipe_container.setOnRefreshListener(this)
//		viewRoot.swipe_container.setColorSchemeColors(
//				viewRoot.resources.getColor(R.color.colorAccent),
//				viewRoot.resources.getColor(R.color.colorChart),
//				viewRoot.resources.getColor(R.color.colorLabelBackground))
//
//		viewRoot.fab.setOnClickListener { v ->
//
//			val pairs = ArrayList<String>()
//			for (pair in presenter.tickers) {
//				pairs.add(pair.cryptoId + pair.countryId)
//			}
//
//			val dialog = CreateTickerDialog.newInstance(pairs)
//			dialog.setTargetFragment(this, 0)
//			dialog.show(fragmentManager, TAG_CREATE_DIALOG)
//		}
//
//		return view
//	}

//	internal var simpleCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//
//		override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
//			return false
//		}
//
//		override fun isItemViewSwipeEnabled(): Boolean {
//			return true
//		}
//
//
//		override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//
//			val pos = viewHolder.adapterPosition
//			cl_ticker!!.tag = pos
//			val tickerItem = rv_ticker_list!!.getViewResolverAtPosition(pos) as TickerItem
//
//			val snackbar = Snackbar.make(cl_ticker,
//					resources.getString(R.string.title_cardview_removed), Snackbar.LENGTH_SHORT)
//					.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
//
//						//                                @Override
//						//                                public void onDismissed(Snackbar transientBottomBar, int event) {
//						//                                    //presenter.deleteTicker(tickerItem.getTickerItem().getId());
//						//                                    System.out.println("onDismissed");
//						//                                }
//
//						override fun onShown(transientBottomBar: Snackbar?) {
//
//							val amount = App.appInstance.preferences
//									.subscribesAmount - tickerItem.labels.size
//							App.appInstance.preferences.subscribesAmount = amount
//
//							presenter.deleteTicker(tickerItem.tickerItem.id)
//							rv_ticker_list!!.removeView(viewHolder.adapterPosition)
//
//							updateRecyclerVisibility()
//						}
//					})
//			snackbar.show()
//
//		}
//
//		override fun onChildDraw(c: Canvas, recyclerView: RecyclerView,
//								 viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
//								 actionState: Int, isCurrentlyActive: Boolean) {
//
//			if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//				val alpha = 1 - Math.abs(dX) / recyclerView.width
//				viewHolder.itemView.alpha = alpha
//			}
//
//			super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//		}
//	}
	
	override fun showError(error: String) {
		Toast.makeText(viewRoot.context, error, Toast.LENGTH_SHORT).show()
	}
	
	override fun showError(res: Int) {
		val text = viewRoot.context.getString(res)
		Toast.makeText(viewRoot.context, text, Toast.LENGTH_SHORT).show()
	}
	
	override fun setRefreshing(key: Boolean) {
		//viewRoot.refreshLayout.isRefreshing = key
	}
	
	override fun addTickerToAdapter(ticker: Ticker) {
		Log.i(tag, "addTickerToAdapter()")
		
		val price = ticker.ticker?.price
		price?.let {
			val text = PriceConverter.convertPrice(price)
			val tickerItem = TickerItem(viewRoot.context, viewRoot.rv_ticker_list, ticker)
			tickerItem.setPrice(text)
			//tickerItem.setPriceChange(ticker.ticker.getPercentChange(preferences.showedPriceChange)) todo
			
			viewRoot.rv_ticker_list.addView(tickerItem)
			scrollToPosition(viewRoot.rv_ticker_list.allViewResolvers.size - 1)
			updateRecyclerVisibility()
		}
	}
	
	override fun addAllTickers(tickers: List<Ticker>, subscribes: List<Subscribe>) {
		Log.i(tag, "addAllTickers()")
		resetFeed()
		
		for (item in presenter.sortTickers(tickers)) {
			
			val itemSubs = Stream.of(subscribes)
					.filter { s -> s.tickerId == item.id }
					.toList()
			
			item.ticker?.price?.let {
				val text = PriceConverter.convertPrice(it)
				
				val tickerItem = TickerItem(viewRoot.context, viewRoot.rv_ticker_list, item, itemSubs)
				tickerItem.setPrice(text)
				//tickerItem.setPriceChange(item.ticker.getPercentChange(preferences.showedPriceChange)) todo
				
				viewRoot.rv_ticker_list.addView(tickerItem)
			}
		}
		
		updateRecyclerVisibility()
	}
	
}