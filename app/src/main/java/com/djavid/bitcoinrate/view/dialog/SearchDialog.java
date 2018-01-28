package com.djavid.bitcoinrate.view.dialog;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.view.adapter.SearchAdapter;

import java.util.ArrayList;

import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.FilterResultListener;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;


public class SearchDialog<T extends Searchable> extends BaseSearchDialogCompat<T> {

    private String mTitle;
    private String mSearchHint;
    private SearchResultListener<T> mSearchResultListener;

    private TextView mTxtTitle;
    private EditText mSearchBox;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    // In case you are doing process in another thread
    // and wanted to update the progress in that thread
    private Handler mHandler;


    public SearchDialog(Context context, String title, String searchHint, @Nullable Filter filter,
                        ArrayList<T> items, SearchResultListener<T> searchResultListener) {

        super(context, items, filter, null, null);
        init(title, searchHint, searchResultListener);
    }

    private void init(String title, String searchHint, SearchResultListener<T> searchResultListener) {

        mTitle = title;
        mSearchHint = searchHint;
        mSearchResultListener = searchResultListener;

        mHandler = new Handler();

        setFilterResultListener(new FilterResultListener<T>() {
            @SuppressWarnings("unchecked")
            @Override
            public void onFilter(ArrayList items) {
                ((SearchAdapter<Searchable>) getAdapter())
                        .setSearchTag(mSearchBox.getText().toString())
                        .setItems(items);
            }
        });

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void getView(View view) {

        setContentView(view);
        setCancelable(true);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        mTxtTitle = view.findViewById(R.id.txt_title);
        mSearchBox = view.findViewById(getSearchBoxId());
        mRecyclerView = view.findViewById(getRecyclerViewId());
        mProgressBar = view.findViewById(R.id.progress);

        mTxtTitle.setText(mTitle);
        mSearchBox.setHint(mSearchHint);
        mSearchBox.requestFocus();
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.GONE);

        final SearchAdapter adapter = new SearchAdapter(getContext(),
                R.layout.search_adapter_item, getItems());
        adapter.setSearchResultListener(mSearchResultListener);
        adapter.setSearchDialog(this);
        setAdapter(adapter);

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

    @Override
    protected int getLayoutResId() {
        return R.layout.search_dialog_compat;
    }

    @Override
    protected int getSearchBoxId() {
        return R.id.txt_search;
    }

    @Override
    protected int getRecyclerViewId() {
        return R.id.rv_items;
    }

    private void setLoading(final boolean isLoading) {
        System.out.println("setLoading");

        mHandler.post(() -> {
            if (mRecyclerView != null)
                mRecyclerView.setVisibility(!isLoading ? View.VISIBLE : View.GONE);

            if (mProgressBar != null)
                mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }
}
