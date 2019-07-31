package com.djavid.bitcoinrate.view.ticker

import android.graphics.Canvas
import android.util.Log
import android.view.View
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.app.App
import com.djavid.bitcoinrate.contracts.ticker.TickerContract
import com.djavid.bitcoinrate.model.heroku.Ticker
import java.util.*

class TickerView(
		private val viewRoot: View
) : TickerContract.View {
	
	private val TAG_CREATE_DIALOG = "TAG_CREATE_DIALOG"
	
	private lateinit var presenter: TickerContract.Presenter
	
	override fun init(presenter: TickerContract.Presenter) {
		this.presenter = presenter
		
		if ((activity as AppCompatActivity).supportActionBar != null) {
			(activity as AppCompatActivity).supportActionBar!!.setTitle(R.string.title_cards)
		}
	}
	
	override fun showPopupWindow() {
		val menuItemView = activity.findViewById(R.id.sort)
		val tickerPopupWindow = TickerPopupWindow(menuItemView, context, this)
		tickerPopupWindow.show()
	}
	
	//    override fun showSnackbar(message: String) {
//        val snackbar = Snackbar.make(cl_ticker!!, message, Snackbar.LENGTH_SHORT)
//        snackbar.show()
//    }
	
	override fun updateRecyclerVisibility() {
		
		if (!rv_ticker_list!!.allViewResolvers.isEmpty()) {
			swipe_container!!.visibility = View.VISIBLE
			rl_no_data!!.visibility = View.GONE
		} else {
			swipe_container!!.visibility = View.GONE
			rl_no_data!!.visibility = View.VISIBLE
		}
		
	}
	
	override fun scrollToPosition(position: Int) {
		rv_ticker_list!!.scrollToPosition(position)
	}
	
	override fun addView(item: Ticker) {
		rv_ticker_list!!.addView(item)
	}
	
	override fun resetFeed() {
		rv_ticker_list!!.removeAllViews()
	}
	
	fun setupView(view: View): View {
		Log.i(TAG, "setupView()")
		
		val itemTouchHelper = ItemTouchHelper(simpleCallback)
		itemTouchHelper.attachToRecyclerView(rv_ticker_list)
		
		swipe_container!!.setOnRefreshListener(this)
		swipe_container!!.setColorSchemeColors(
				resources.getColor(R.color.colorAccent),
				resources.getColor(R.color.colorChart),
				resources.getColor(R.color.colorLabelBackground))
		
		fab!!.setOnClickListener { v ->
			
			val pairs = ArrayList<String>()
			for (pair in presenter.tickers) {
				pairs.add(pair.cryptoId + pair.countryId)
			}
			
			val dialog = CreateTickerDialog.newInstance(pairs)
			dialog.setTargetFragment(this, 0)
			dialog.show(fragmentManager, TAG_CREATE_DIALOG)
			
		}
		
		return view
	}
	
	internal var simpleCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
		
		override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
			return false
		}
		
		override fun isItemViewSwipeEnabled(): Boolean {
			return true
		}
		
		
		override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
			
			val pos = viewHolder.adapterPosition
			cl_ticker!!.tag = pos
			val tickerItem = rv_ticker_list!!.getViewResolverAtPosition(pos) as TickerItem
			
			val snackbar = Snackbar.make(cl_ticker,
					resources.getString(R.string.title_cardview_removed), Snackbar.LENGTH_SHORT)
					.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
						
						//                                @Override
						//                                public void onDismissed(Snackbar transientBottomBar, int event) {
						//                                    //presenter.deleteTicker(tickerItem.getTickerItem().getId());
						//                                    System.out.println("onDismissed");
						//                                }
						
						override fun onShown(transientBottomBar: Snackbar?) {
							
							val amount = App.appInstance.preferences
									.subscribesAmount - tickerItem.labels.size
							App.appInstance.preferences.subscribesAmount = amount
							
							presenter.deleteTicker(tickerItem.tickerItem.id)
							rv_ticker_list!!.removeView(viewHolder.adapterPosition)
							
							updateRecyclerVisibility()
						}
					})
			//                            .setAction(getResources().getString(R.string.title_cardview_undo), v -> {
			//                                int position = (int) cl_ticker.getTag();
			//
			//                                TickerItem restoredTickerItem = new TickerItem(getContext(),
			//                                        rv_ticker_list, presenter.getTickersLocal().get(position));
			//                                rv_ticker_list.addView(position, restoredTickerItem);
			//
			//                                System.out.println(presenter.getTickersLocal().get(position));
			//                                rv_ticker_list.refresh();
			//                                presenter.loadTickerPrice(restoredTickerItem);
			//                                scrollToPosition(rv_ticker_list.getAllViewResolvers().size() - 1);
			//                            }); //TODO undo deleting
			snackbar.show()
			
		}
		
		override fun onChildDraw(c: Canvas, recyclerView: RecyclerView,
								 viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
								 actionState: Int, isCurrentlyActive: Boolean) {
			
			if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
				val alpha = 1 - Math.abs(dX) / recyclerView.width
				viewHolder.itemView.alpha = alpha
			}
			
			super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
		}
	}
	
	
}