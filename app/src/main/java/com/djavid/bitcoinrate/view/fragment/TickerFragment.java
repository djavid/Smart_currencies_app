package com.djavid.bitcoinrate.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.adapter.TickerItem;
import com.djavid.bitcoinrate.core.BaseFragment;
import com.djavid.bitcoinrate.domain.MainRouter;
import com.djavid.bitcoinrate.model.realm.TickerItemRealm;
import com.djavid.bitcoinrate.presenter.interfaces.TickerFragmentPresenter;
import com.djavid.bitcoinrate.view.interfaces.TickerFragmentView;
import com.mindorks.placeholderview.PlaceHolderView;

import java.util.List;

import butterknife.BindView;
import io.realm.RealmResults;


public class TickerFragment extends BaseFragment implements TickerFragmentView {

    @BindView(R.id.rv_ticker_list)
    PlaceHolderView rv_ticker_list;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.cl_ticker)
    CoordinatorLayout cl_ticker;

    TickerFragmentPresenter presenter;
    private OnTickerInteractionListener mTickerListener;


    public TickerFragment() { }

    public static TickerFragment newInstance() {
        TickerFragment fragment = new TickerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_ticker;
    }

    @Override
    public String getPresenterId() {
        return "ticker_fragment";
    }

    @Override
    public void onStart() {
        presenter = getPresenter(TickerFragmentPresenter.class);
        presenter.setView(this);
        presenter.setRouter((MainRouter) getActivity());
        presenter.onStart();

        super.onStart();
    }

    @Override
    public void onStop() {
        presenter.setView(null);
        resetFeed();

        presenter.onStop();
        super.onStop();
    }

    @Override
    public View setupView(View view) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv_ticker_list);

        fab.setOnClickListener(v -> {
            presenter.getRouter().showCreateDialog();
        });

        return view;
    }

    @Override
    public void loadData() {
        addViewsFromRealm();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTickerInteractionListener) {
            mTickerListener = (OnTickerInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTickerInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTickerListener = null;

    }

    public interface OnTickerInteractionListener {
        void onFragmentInteraction(TickerItemRealm item);
    }

    @Override
    public void scrollToPosition(int position) {
        rv_ticker_list.scrollToPosition(position);
    }

    @Override
    public void addView(TickerItemRealm item) {
        rv_ticker_list.addView(item);
    }

    @Override
    public void resetFeed() {
        rv_ticker_list.removeAllViews();
    }

    @Override
    public void refreshFeed(RealmResults<TickerItemRealm> tickerItemRealms) {
        List tickers = rv_ticker_list.getAllViewResolvers();

        if (tickerItemRealms.size() - tickers.size() == 1) {
            TickerItem tickerItem = new TickerItem(getContext(), rv_ticker_list, tickerItemRealms.last());
            rv_ticker_list.addView(tickerItem);
            presenter.loadTickerPrice(tickerItem);

            scrollToPosition(tickerItemRealms.size() - 1);
            rv_ticker_list.refresh();
        }
    }

    private void addViewsFromRealm() {
        RealmResults<TickerItemRealm> tickers = presenter.getAllTickers();
        resetFeed();

        for (TickerItemRealm item : tickers) {
            TickerItem tickerItem = new TickerItem(getContext(), rv_ticker_list, item);
            rv_ticker_list.addView(tickerItem);
            presenter.loadTickerPrice(tickerItem);
        }
    }


    ItemTouchHelper.SimpleCallback simpleCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            TickerItemRealm itemRealm = ((TickerItem) rv_ticker_list
                    .getViewResolverAtPosition(viewHolder.getAdapterPosition())).getRealm();
            presenter.deleteTicker(itemRealm);

            rv_ticker_list.removeView(viewHolder.getAdapterPosition());
            rv_ticker_list.refresh();
        }
    };



    @Override
    public void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(cl_ticker, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
