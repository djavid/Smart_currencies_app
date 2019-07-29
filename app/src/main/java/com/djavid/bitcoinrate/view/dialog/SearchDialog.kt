package com.djavid.bitcoinrate.view.dialog

import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.Filter
import android.widget.ProgressBar
import android.widget.TextView
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.view.adapter.SearchAdapter
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat
import ir.mirrajabi.searchdialog.core.FilterResultListener
import ir.mirrajabi.searchdialog.core.SearchResultListener
import ir.mirrajabi.searchdialog.core.Searchable
import java.util.*


class SearchDialog<T : Searchable>(context: Context, title: String, searchHint: String, filter: Filter?,
                                   items: ArrayList<T>, searchResultListener: SearchResultListener<T>) : BaseSearchDialogCompat<T>(context, items, filter, null, null) {

    private var mTitle: String? = null
    private var mSearchHint: String? = null
    private var mSearchResultListener: SearchResultListener<T>? = null

    private var mTxtTitle: TextView? = null
    private var mSearchBox: EditText? = null
    private var mRecyclerView: RecyclerView? = null
    private var mProgressBar: ProgressBar? = null

    // In case you are doing process in another thread
    // and wanted to update the progress in that thread
    private var mHandler: Handler? = null


    init {
        init(title, searchHint, searchResultListener)
    }

    private fun init(title: String, searchHint: String, searchResultListener: SearchResultListener<T>) {

        mTitle = title
        mSearchHint = searchHint
        mSearchResultListener = searchResultListener

        mHandler = Handler()

        setFilterResultListener(object : FilterResultListener<T> {
            override fun onFilter(items: ArrayList<*>) {
                (adapter as SearchAdapter<Searchable>)
                        .setSearchTag(mSearchBox!!.text.toString())
                        .items = items
            }
        })

    }

    override fun getView(view: View) {

        setContentView(view)
        setCancelable(true)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)

        mTxtTitle = view.findViewById(R.id.txt_title)
        mSearchBox = view.findViewById(searchBoxId)
        mRecyclerView = view.findViewById(recyclerViewId)
        mProgressBar = view.findViewById(R.id.progress)

        mTxtTitle!!.text = mTitle
        mSearchBox!!.hint = mSearchHint
        mSearchBox!!.requestFocus()
        mProgressBar!!.isIndeterminate = true
        mProgressBar!!.visibility = View.GONE

        val adapter = SearchAdapter(context,
                R.layout.search_adapter_item, items)
        adapter.searchResultListener = mSearchResultListener
        adapter.setSearchDialog(this)
        setAdapter(adapter)

        //        ((BaseFilter<T>) getFilter()).setOnPerformFilterListener(new OnPerformFilterListener() {
        //            @Override
        //            public void doBeforeFiltering() {
        //                setLoading(true);
        //            }
        //
        //            @Override
        //            public void doAfterFiltering() {
        //                setLoading(false);
        //            }
        //        });

    }

    override fun getLayoutResId(): Int {
        return R.layout.search_dialog_compat
    }

    override fun getSearchBoxId(): Int {
        return R.id.txt_search
    }

    override fun getRecyclerViewId(): Int {
        return R.id.rv_items
    }

    private fun setLoading(isLoading: Boolean) {
        println("setLoading")

        mHandler!!.post {
            if (mRecyclerView != null)
                mRecyclerView!!.visibility = if (!isLoading) View.VISIBLE else View.GONE

            if (mProgressBar != null)
                mProgressBar!!.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}
