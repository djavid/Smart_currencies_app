package com.djavid.bitcoinrate.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.model.project.Coin;
import com.djavid.bitcoinrate.util.Codes;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.StringsHelper;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;


public class SearchAdapter<T extends Searchable> extends
        RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    protected Context mContext;
    private List<T> mItems = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private int mLayout;
    private SearchResultListener mSearchResultListener;
    private AdapterViewBinder<T> mViewBinder;
    private String mSearchTag;
    private boolean mHighlightPartsInCommon = true;
    private String mHighlightColor = "#FFED2E47";
    private BaseSearchDialogCompat mSearchDialog;


    public SearchAdapter(Context context, @LayoutRes int layout, List<T> items) {
        this(context, layout, null, items);
    }

    public SearchAdapter(Context context, AdapterViewBinder<T> viewBinder,
                         @LayoutRes int layout, List<T> items) {
        this(context, layout, viewBinder, items);
    }

    public SearchAdapter(Context context, @LayoutRes int layout,
                         @Nullable AdapterViewBinder<T> viewBinder, List<T> items) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mItems = items;
        this.mLayout = layout;
        this.mViewBinder = viewBinder;
    }

    public List<T> getItems() {
        return mItems;
    }

    public void setItems(List<T> objects) {
        this.mItems = objects;
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public SearchAdapter<T> setViewBinder(AdapterViewBinder<T> viewBinder) {
        this.mViewBinder = viewBinder;
        return this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mLayoutInflater.inflate(mLayout, parent, false);
        convertView.setTag(new ViewHolder(convertView));
        return (ViewHolder) convertView.getTag();
    }

    @Override
    public void onBindViewHolder(SearchAdapter.ViewHolder holder, int position) {
        initializeViews(getItem(position), holder, position);
    }

    private void initializeViews(final T object, final SearchAdapter.ViewHolder holder,
                                 final int position) {

        if (mViewBinder != null) mViewBinder.bind(holder, object, position);

        LinearLayout root = holder.getViewById(R.id.ll_search_root);
        TextView text = holder.getViewById(R.id.tv_search_name);
        ImageView image = holder.getViewById(R.id.iv_search_image);

        if (position % 2 == 0)
            root.setBackgroundColor(Color.parseColor("#f6f6f6"));
        else
            root.setBackgroundColor(Color.parseColor("#fcfcfc"));

        if (!((Coin) object).imageUrl.isEmpty()) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(((Coin) object).imageUrl)
                    .into(image);
        } else {
            Glide.with(mContext)
                    .asBitmap()
                    .load(Codes.getCountryImage(((Coin) object).symbol))
                    .into(image);
        }

        if (mSearchTag != null && mHighlightPartsInCommon)
            text.setText(
                    StringsHelper.highlightLCS(object.getTitle(),
                            getSearchTag(),
                            Color.parseColor(mHighlightColor)));
        else
            text.setText(object.getTitle());

        if (mSearchResultListener != null)
            holder.getBaseView().setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("unchecked")
                @Override
                public void onClick(View view) {
                    mSearchResultListener.onSelected(mSearchDialog, object, position);
                }
            });
    }

    public SearchResultListener getSearchResultListener() {
        return mSearchResultListener;
    }

    public void setSearchResultListener(SearchResultListener searchResultListener) {
        this.mSearchResultListener = searchResultListener;
    }

    public SearchAdapter<T> setSearchTag(String searchTag) {
        mSearchTag = searchTag;
        return this;
    }

    public String getSearchTag() {
        return mSearchTag;
    }

    public SearchAdapter setHighlightPartsInCommon(boolean highlightPartsInCommon) {
        mHighlightPartsInCommon = highlightPartsInCommon;
        return this;
    }

    public boolean isHighlightPartsInCommon() {
        return mHighlightPartsInCommon;
    }

    public SearchAdapter setHighlightColor(String highlightColor) {
        mHighlightColor = highlightColor;
        return this;
    }

    public SearchAdapter setSearchDialog(BaseSearchDialogCompat searchDialog) {
        mSearchDialog = searchDialog;
        return this;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View mBaseView;

        public ViewHolder(View view) {
            super(view);
            mBaseView = view;
        }

        public View getBaseView() {
            return mBaseView;
        }

        @SuppressWarnings("unchecked")
        public <T> T getViewById(@IdRes int id) {
            return (T) mBaseView.findViewById(id);
        }

        public void clearAnimation(@IdRes int id) {
            mBaseView.findViewById(id).clearAnimation();
        }
    }

    public interface AdapterViewBinder<T> {
        void bind(ViewHolder holder, T item, int position);
    }
}
