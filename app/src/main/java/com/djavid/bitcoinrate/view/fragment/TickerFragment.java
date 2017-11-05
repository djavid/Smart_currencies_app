package com.djavid.bitcoinrate.view.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.mindorks.placeholderview.PlaceHolderView;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import io.realm.RealmResults;


public class TickerFragment extends BaseFragment implements TickerFragmentView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_ticker_list)
    PlaceHolderView rv_ticker_list;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.cl_ticker)
    CoordinatorLayout cl_ticker;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipe_container;

    TickerFragmentPresenter presenter;
    private OnTickerInteractionListener mTickerListener;


    public TickerFragment() {
    }

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

        swipe_container.setOnRefreshListener(this);
        swipe_container.setColorSchemeColors(
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorChart),
                getResources().getColor(R.color.colorLabelBackground));

        fab.setOnClickListener(v -> {
            presenter.getRouter().showCreateDialog();
        });

        return view;
    }

    @Override
    public void loadData() {
        addViewsFromRealm();
        System.out.println(FirebaseInstanceId.getInstance().getToken() + "---------------------------------------------------------------");
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
    public void onRefresh() {
        List tickers = rv_ticker_list.getAllViewResolvers();

        for (Object obj : tickers) {
            TickerItem item = ((TickerItem) obj);
            presenter.loadTickerPrice(item);
        }
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
        }
    }

    private void addViewsFromRealm() {
        resetFeed();
        RealmResults<TickerItemRealm> tickers = presenter.getAllTickers();

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

                    int pos = viewHolder.getAdapterPosition();
                    cl_ticker.setTag(pos);

                    TickerItem tickerItem = (TickerItem) rv_ticker_list.getViewResolverAtPosition(pos);
                    Date createdAt = tickerItem.getCreatedAt();

                    Snackbar snackbar = Snackbar.make(cl_ticker,
                            getResources().getString(R.string.title_cardview_removed), Snackbar.LENGTH_SHORT)
                            .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
//                        @Override
//                        public void onDismissed(Snackbar transientBottomBar, int event) {
//                            System.out.println(createdAt.toString());
//                            presenter.deleteTicker(createdAt);
//                        }

                                @Override
                                public void onShown(Snackbar transientBottomBar) {
                                    int position = (int) cl_ticker.getTag();
                                    presenter.deleteTicker(createdAt);
                                    rv_ticker_list.removeView(viewHolder.getAdapterPosition());
                                }
                            });
//                    .setAction(getResources().getString(R.string.title_cardview_undo), v -> {
//                        int position = (int) cl_ticker.getTag();
//
//                        presenter.getRealm().executeTransaction(realm -> {
//                            TickerItemRealm ticker = realm
//                                    .where(TickerItemRealm.class)
//                                    .equalTo("createdAt", createdAt).findFirst();
//
//                            TickerItem restoredTickerItem = new TickerItem(getContext(), rv_ticker_list, ticker);
//                            rv_ticker_list.addView(position, tickerItem);
//                            rv_ticker_list.refresh();
//                            presenter.loadTickerPrice(tickerItem);
//                        });
//                    });
                    snackbar.show();

                }

                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                        RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                        int actionState, boolean isCurrentlyActive) {

                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                        float alpha = 1 - (Math.abs(dX) / recyclerView.getWidth());
                        viewHolder.itemView.setAlpha(alpha);
                    }

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            };


    @Override
    public void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(cl_ticker, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public SwipeRefreshLayout getRefreshLayout() {
        return swipe_container;
    }
}
