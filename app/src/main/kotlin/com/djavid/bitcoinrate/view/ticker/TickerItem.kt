package com.djavid.bitcoinrate.view.ticker

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.app.App
import com.djavid.bitcoinrate.model.heroku.Subscribe
import com.djavid.bitcoinrate.model.heroku.Ticker
import com.djavid.bitcoinrate.model.project.LabelItemDto
import com.djavid.bitcoinrate.util.Codes
import com.djavid.bitcoinrate.util.PriceConverter
import com.mindorks.placeholderview.PlaceHolderView
import com.mindorks.placeholderview.annotations.Layout
import com.mindorks.placeholderview.annotations.NonReusable
import com.mindorks.placeholderview.annotations.Resolve
import com.mindorks.placeholderview.annotations.View
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.*


@NonReusable
@Layout(R.layout.recycler_item_ticker)
class TickerItem {

    @View(R.id.tv_ticker_title)
    private val tv_ticker_title: TextView? = null
    @View(R.id.tv_price_change)
    private val tv_price_change: TextView? = null
    @View(R.id.tv_market_cap)
    private val tv_market_cap: TextView? = null
    @View(R.id.iv_ticker_icon)
    private val iv_ticker_icon: ImageView? = null
    @View(R.id.tickerValue)
    private val tickerValue: TextView? = null
    @View(R.id.label_container)
    private val label_container: PlaceHolderView? = null
    @View(R.id.v_divider2)
    private val v_divider2: android.view.View? = null

    var tickerItem: Ticker? = null
        private set
    private var labels: MutableList<LabelItemDto>? = null

    private val TAG = this.javaClass.simpleName
    private var mContext: Context? = null
    private var mPlaceHolderView: PlaceHolderView? = null
    private var tv_price: String? = null
    private var price_change_color: Int = 0
    private var price_change: String? = null
    internal var mObservable: Subject<String> = PublishSubject.create()


    constructor(mContext: Context, mPlaceHolderView: PlaceHolderView, tickerItem: Ticker,
                subscribes: List<Subscribe>) {
        this.mContext = mContext
        this.mPlaceHolderView = mPlaceHolderView
        this.tickerItem = tickerItem

        labels = ArrayList()
        for (item in subscribes) {
            labels!!.add(LabelItemDto(item.id!!, item.value,
                    item.isTrendingUp, item.change_percent))
        }
        labels = sortLabels(labels)
    }

    constructor(mContext: Context, mPlaceHolderView: PlaceHolderView, tickerItem: Ticker) {
        this.tickerItem = tickerItem
        this.mContext = mContext
        this.mPlaceHolderView = mPlaceHolderView
        labels = ArrayList()
    }

    @Resolve
    private fun onResolved() {

        try {

            val title = Codes.getCryptoCurrencyId(tickerItem!!.cryptoId)
            if (title.length > 12) {
                tv_ticker_title!!.textSize = 13f
            }
            tv_ticker_title!!.text = title

            tv_market_cap!!.text = PriceConverter.convertMarketCap(tickerItem!!.ticker!!.market_cap_usd)

            println(tickerItem!!.cryptoId)
            val image_url = Codes.getCryptoCoinImage(tickerItem!!.cryptoId)
            Glide.with(mContext!!)
                    .asBitmap()
                    .load(image_url)
                    .into(iv_ticker_icon!!)

            tickerValue!!.text = tv_price
            tv_price_change!!.setTextColor(price_change_color)
            tv_price_change.text = price_change

            val layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            label_container!!.layoutManager = layoutManager


            for (item in labels!!) {
                label_container.addView(LabelItem(mContext, label_container, item, this))
            }
            label_container.addView(LabelItem(mContext, label_container, LabelItemDto(), this))

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun sortLabels(subscribes: List<LabelItemDto>): MutableList<LabelItemDto> {

        val sorted_subscribes: List<LabelItemDto>

//        sorted_subscribes = Stream.of(subscribes)
//                .sorted { a, b ->
//
//                    if (a.change_percent == 0.0 && b.change_percent == 0.0) {
//
//                        return@Stream.of(subscribes)
//                                .sorted a . value !!. compareTo b.value!!
//
//                    } else if (a.change_percent != 0.0 && b.change_percent != 0.0) {
//
//                        return@Stream.of(subscribes)
//                                .sorted java . lang . Double . compare a.change_percent, b.change_percent)
//
//                    } else if (a.change_percent == 0.0 && b.change_percent != 0.0) {
//
//                        return@Stream.of(subscribes)
//                                .sorted 1
//
//                    } else if (a.change_percent != 0.0 && b.change_percent == 0.0) {
//
//                        return@Stream.of(subscribes)
//                                .sorted - 1
//
//                    } else {
//
//                        return@Stream.of(subscribes)
//                                .sorted 0
//
//                    }
//
//                }
//                .collect<List<LabelItemDto>, Any>(Collectors.toList())
	
		return emptyList<LabelItemDto>().toMutableList()
    }

    fun getLabels(): List<LabelItemDto>? {
        return labels
    }


    fun addLabelItem(new_item: LabelItemDto) {

        labels!!.add(new_item)

        label_container!!.removeAllViews() //is it needed?
        for (item in labels!!) {
            label_container.addView(LabelItem(mContext, label_container, item, this))
        }
        label_container.addView(LabelItem(mContext, label_container, LabelItemDto(), this))

    }

    internal fun deleteLabel(labelItem: LabelItem) {
        labels!!.remove(labelItem.labelItemDto)
        label_container!!.removeView(labelItem)
    }

    fun setPrice(price: String) {
        if (tickerItem != null)
            tv_price = price + " " + tickerItem!!.countryId

        if (tickerValue != null) {
            tickerValue.text = tv_price
        }
    }

    fun setPriceChange(change: Double) {

        Log.i(TAG, "setPriceChange($change)")

        if (change > 0) {
            price_change = "+$change%"
            price_change_color = App.context.resources.getColor(R.color.colorPriceChangePos)
        } else {
            price_change = "$change%"
            price_change_color = App.context.resources.getColor(R.color.colorPriceChangeNeg)
        }

        if (tv_price_change != null) {
            tv_price_change.setTextColor(price_change_color)
            tv_price_change.text = price_change
        }
    }

}
