package com.djavid.bitcoinrate.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import butterknife.BindView
import com.annimon.stream.Stream
import com.djavid.bitcoinrate.App
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.core.BaseDialog
import com.djavid.bitcoinrate.core.Router
import com.djavid.bitcoinrate.model.heroku.Subscribe
import com.djavid.bitcoinrate.model.heroku.Ticker
import com.djavid.bitcoinrate.presenter.interfaces.TickerFragmentPresenter
import com.djavid.bitcoinrate.util.PriceConverter
import com.djavid.bitcoinrate.view.adapter.TickerItem
import com.djavid.bitcoinrate.view.dialog.CreateTickerDialog
import com.djavid.bitcoinrate.view.dialog.TickerPopupWindow
import com.djavid.bitcoinrate.view.interfaces.TickerFragmentView
import com.mindorks.placeholderview.PlaceHolderView
import java.util.*

class TickerFragment : BaseDialog(), TickerFragmentView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_ticker_list)
    internal var rv_ticker_list: PlaceHolderView? = null
    @BindView(R.id.fab)
    internal var fab: FloatingActionButton? = null
    @BindView(R.id.cl_ticker)
    internal var cl_ticker: CoordinatorLayout? = null
    @BindView(R.id.swipe_container)
    internal var swipe_container: SwipeRefreshLayout? = null
    @BindView(R.id.rl_no_data)
    internal var rl_no_data: RelativeLayout? = null

    private val TAG = this.javaClass.simpleName
    private val TAG_CREATE_DIALOG = "TAG_CREATE_DIALOG"

    var presenter: TickerFragmentPresenter? = null
        private set


    val layoutId: Int
        get() = R.layout.fragment_ticker

    val presenterId: String
        get() = "ticker_fragment"

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

                            presenter!!.deleteTicker(tickerItem.tickerItem.id)
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

    fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_tickers, menu)
    }

    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> presenter!!.getAllTickers(true)

            R.id.sort -> {

                val menuItemView = activity.findViewById(R.id.sort)
                val tickerPopupWindow = TickerPopupWindow(menuItemView, context, this)
                tickerPopupWindow.show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun onStart() {
        Log.i(TAG, "onStart()")
        presenter = getPresenter(TickerFragmentPresenter::class.java)
        presenter!!.view = this
        presenter!!.router = activity as Router
        presenter!!.onStart()

        super.onStart()

        if ((activity as AppCompatActivity).supportActionBar != null) {
            (activity as AppCompatActivity).supportActionBar!!.setTitle(R.string.title_cards)
        }
    }

    fun onStop() {
        presenter!!.view = null
        presenter!!.onStop()

        super.onStop()
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
            for (pair in presenter!!.tickers) {
                pairs.add(pair.cryptoId + pair.countryId)
            }

            val dialog = CreateTickerDialog.newInstance(pairs)
            dialog.setTargetFragment(this, 0)
            dialog.show(fragmentManager, TAG_CREATE_DIALOG)

        }

        return view
    }

    fun loadData() {
        Log.i(TAG, "loadData()")
        presenter!!.getAllTickers(false)
    }

    override fun onRefresh() {
        presenter!!.getAllTickers(true)
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

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

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

    override fun updateRecyclerVisibility() {

        if (!rv_ticker_list!!.allViewResolvers.isEmpty()) {
            swipe_container!!.visibility = View.VISIBLE
            rl_no_data!!.visibility = View.GONE
        } else {
            swipe_container!!.visibility = View.GONE
            rl_no_data!!.visibility = View.VISIBLE
        }

    }

    override fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(cl_ticker!!, message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    override fun getRefreshLayout(): SwipeRefreshLayout? {
        return swipe_container
    }

    companion object {

        fun newInstance(): TickerFragment {
            val fragment = TickerFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


}
