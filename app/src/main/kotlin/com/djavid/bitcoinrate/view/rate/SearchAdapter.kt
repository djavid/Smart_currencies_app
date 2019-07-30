package com.djavid.bitcoinrate.view.rate

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.model.project.Coin
import com.djavid.bitcoinrate.util.Codes
import ir.mirrajabi.searchdialog.StringsHelper
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat
import ir.mirrajabi.searchdialog.core.SearchResultListener
import ir.mirrajabi.searchdialog.core.Searchable
import java.util.*


class SearchAdapter<T : Searchable>(protected var mContext: Context, @param:LayoutRes private val mLayout: Int,
									private var mViewBinder: AdapterViewBinder<T>?, items: List<T>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private var mItems: List<T> = ArrayList()
    private val mLayoutInflater: LayoutInflater
    var searchResultListener: SearchResultListener<*>? = null
    private var mSearchTag: String? = null
    private var mHighlightPartsInCommon = true
    private var mHighlightColor = "#FFED2E47"
    private var mSearchDialog: BaseSearchDialogCompat<*>? = null

    var items: List<T>
        get() = mItems
        set(objects) {
            this.mItems = objects
            notifyDataSetChanged()
        }


    constructor(context: Context, @LayoutRes layout: Int, items: List<T>) : this(context, layout, null, items)

    constructor(context: Context, viewBinder: AdapterViewBinder<T>,
				@LayoutRes layout: Int, items: List<T>) : this(context, layout, viewBinder, items)

    init {
        this.mLayoutInflater = LayoutInflater.from(mContext)
        this.mItems = items
    }

    fun getItem(position: Int): T {
        return mItems[position]
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setViewBinder(viewBinder: AdapterViewBinder<T>): SearchAdapter<T> {
        this.mViewBinder = viewBinder
        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val convertView = mLayoutInflater.inflate(mLayout, parent, false)
        convertView.tag = ViewHolder(convertView)
        return convertView.tag as ViewHolder
    }
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        initializeViews(getItem(position), holder, position)
    }
	
	private fun initializeViews(`object`: T, holder: ViewHolder,
								position: Int) {

        if (mViewBinder != null) mViewBinder!!.bind(holder, `object`, position)

        val root = holder.getViewById<LinearLayout>(R.id.ll_search_root)
        val text = holder.getViewById<TextView>(R.id.tv_search_name)
        val image = holder.getViewById<ImageView>(R.id.iv_search_image)

        if (position % 2 == 0)
            root.setBackgroundColor(Color.parseColor("#f6f6f6"))
        else
            root.setBackgroundColor(Color.parseColor("#fcfcfc"))

        if (!(`object` as Coin).imageUrl.isEmpty()) {
            Glide.with(mContext)
                    .asBitmap()
                    .load((`object` as Coin).imageUrl)
                    .into(image)
        } else {
            Glide.with(mContext)
                    .asBitmap()
                    .load(Codes.getCountryImage((`object` as Coin).symbol))
                    .into(image)
        }

        if (mSearchTag != null && mHighlightPartsInCommon)
            text.text = StringsHelper.highlightLCS(`object`.title,
                    getSearchTag()!!,
                    Color.parseColor(mHighlightColor))
        else
            text.text = `object`.title

        if (searchResultListener != null)
            holder.baseView.setOnClickListener { searchResultListener!!.onSelected(mSearchDialog, `object`, position) }
    }

    fun setSearchTag(searchTag: String): SearchAdapter<T> {
        mSearchTag = searchTag
        return this
    }

    fun getSearchTag(): String? {
        return mSearchTag
    }

    fun setHighlightPartsInCommon(highlightPartsInCommon: Boolean): SearchAdapter<*> {
        mHighlightPartsInCommon = highlightPartsInCommon
        return this
    }

    fun isHighlightPartsInCommon(): Boolean {
        return mHighlightPartsInCommon
    }

    fun setHighlightColor(highlightColor: String): SearchAdapter<*> {
        mHighlightColor = highlightColor
        return this
    }

    fun setSearchDialog(searchDialog: BaseSearchDialogCompat<*>): SearchAdapter<*> {
        mSearchDialog = searchDialog
        return this
    }

    class ViewHolder(val baseView: View) : RecyclerView.ViewHolder(baseView) {

        fun <T> getViewById(@IdRes id: Int): T {
            return baseView.findViewById<View>(id) as T
        }

        fun clearAnimation(@IdRes id: Int) {
            baseView.findViewById<View>(id).clearAnimation()
        }
    }

    interface AdapterViewBinder<T> {
        fun bind(holder: ViewHolder, item: T, position: Int)
    }
}
