package com.djavid.bitcoinrate.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.annimon.stream.Stream;
import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.core.BaseFragment;
import com.djavid.bitcoinrate.core.Router;
import com.djavid.bitcoinrate.model.dto.heroku.Subscribe;
import com.djavid.bitcoinrate.model.dto.heroku.Ticker;
import com.djavid.bitcoinrate.presenter.interfaces.TickerFragmentPresenter;
import com.djavid.bitcoinrate.util.DateFormatter;
import com.djavid.bitcoinrate.view.adapter.TickerItem;
import com.djavid.bitcoinrate.view.dialog.CreateTickerDialog;
import com.djavid.bitcoinrate.view.dialog.TickerPopupWindow;
import com.djavid.bitcoinrate.view.interfaces.TickerFragmentView;
import com.mindorks.placeholderview.PlaceHolderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


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
    TickerPopupWindow tickerPopupWindow;
    private OnTickerInteractionListener mTickerListener;

    private final String TAG = this.getClass().getSimpleName();
    final String TAG_CREATE_DIALOG = "TAG_CREATE_DIALOG";


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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_tickers, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                presenter.getAllTickers(true);
                break;

            case R.id.sort:

                View menuItemView = getActivity().findViewById(R.id.sort);
                tickerPopupWindow = new TickerPopupWindow(menuItemView, getContext(), this);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart()");
        presenter = getPresenter(TickerFragmentPresenter.class);
        presenter.setView(this);
        presenter.setRouter((Router) getActivity());
        presenter.onStart();

        super.onStart();

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_cards);
        }
    }

    @Override
    public void onStop() {
        presenter.setView(null);
        presenter.onStop();

        super.onStop();
    }

    @Override
    public View setupView(View view) {
        Log.i(TAG, "setupView()");

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv_ticker_list);

        swipe_container.setOnRefreshListener(this);
        swipe_container.setColorSchemeColors(
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorChart),
                getResources().getColor(R.color.colorLabelBackground));

        fab.setOnClickListener(v -> {

            ArrayList<String> pairs = new ArrayList<>();
            for (Ticker pair : presenter.getTickers()) {
                pairs.add(pair.getCryptoId() + pair.getCountryId());
            }

            CreateTickerDialog dialog = CreateTickerDialog.newInstance(pairs);
            dialog.setTargetFragment(this, 0);
            dialog.show(getFragmentManager(), TAG_CREATE_DIALOG);

        });

        return view;
    }

    @Override
    public void loadData() {
        Log.i(TAG, "loadData()");
        presenter.getAllTickers(false);
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
        void onFragmentInteraction(Ticker item);
    }

    @Override
    public void onRefresh() {
        presenter.getAllTickers(true);
    }

    @Override
    public void scrollToPosition(int position) {
        rv_ticker_list.scrollToPosition(position);
    }

    @Override
    public void addView(Ticker item) {
        rv_ticker_list.addView(item);
    }

    @Override
    public void resetFeed() {
        rv_ticker_list.removeAllViews();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {

            if (resultCode == Activity.RESULT_OK) {

                if (data.getExtras() != null && data.getExtras().containsKey("countryId") &&
                        data.getExtras().containsKey("cryptoId") && data.getExtras().containsKey("id")) {

                    long ticker_id = data.getExtras().getLong("id");
                    long token_id = App.getAppInstance().getPreferences().getTokenId();

                    presenter.addTickerFromServer(token_id, ticker_id);
                }
            }
        }
    }

    @Override
    public void addTickerToAdapter(Ticker ticker) {
        Log.i(TAG, "addTickerToAdapter()");

        double price = ticker.getTicker().getPrice();
        String text = DateFormatter.convertPrice(price);

        TickerItem tickerItem = new TickerItem(getContext(), rv_ticker_list, ticker);
        tickerItem.setPrice(text);
        tickerItem.setPriceChange(ticker.getTicker().getPercentChange(
                App.getAppInstance().getPreferences().getShowedPriceChange()));

        rv_ticker_list.addView(tickerItem);
        scrollToPosition(rv_ticker_list.getAllViewResolvers().size() - 1);
    }

    @Override
    public void addAllTickers(List<Ticker> tickers, List<Subscribe> subscribes) {
        Log.i(TAG, "addAllTickers()");
        resetFeed();

        tickers = presenter.sortTickers(tickers);

        for (Ticker item : tickers) {

            List<Subscribe> itemSubs = Stream.of(subscribes)
                    .filter(s -> s.getTickerId() == item.getId())
                    .toList();

            double price = item.getTicker().getPrice();
            String text = DateFormatter.convertPrice(price);

            TickerItem tickerItem = new TickerItem(getContext(), rv_ticker_list, item, itemSubs);
            tickerItem.setPrice(text);
            tickerItem.setPriceChange(item.getTicker().getPercentChange(
                    App.getAppInstance().getPreferences().getShowedPriceChange()));

            rv_ticker_list.addView(tickerItem);
        }
    }

    ItemTouchHelper.SimpleCallback simpleCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                    return false;
                }

                @Override
                public boolean isItemViewSwipeEnabled() {

                    return true;
                }


                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                    System.out.println(viewHolder);

                    int pos = viewHolder.getAdapterPosition();
                    cl_ticker.setTag(pos);
                    TickerItem tickerItem = (TickerItem) rv_ticker_list.getViewResolverAtPosition(pos);

                    Snackbar snackbar = Snackbar.make(cl_ticker,
                            getResources().getString(R.string.title_cardview_removed), Snackbar.LENGTH_SHORT)
                            .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {

//                                @Override
//                                public void onDismissed(Snackbar transientBottomBar, int event) {
//                                    //presenter.deleteTicker(tickerItem.getTickerItem().getId());
//                                    System.out.println("onDismissed");
//                                }

                                @Override
                                public void onShown(Snackbar transientBottomBar) {
                                    presenter.deleteTicker(tickerItem.getTickerItem().getId());
                                    rv_ticker_list.removeView(viewHolder.getAdapterPosition());
                                }
                            });
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

    public TickerFragmentPresenter getPresenter() {
        return presenter;
    }
}
