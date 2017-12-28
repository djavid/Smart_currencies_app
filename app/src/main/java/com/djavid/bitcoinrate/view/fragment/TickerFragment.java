package com.djavid.bitcoinrate.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;

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
import com.djavid.bitcoinrate.view.interfaces.TickerFragmentView;
import com.mindorks.placeholderview.PlaceHolderView;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import java.util.List;

import butterknife.BindView;
import info.hoang8f.android.segmented.SegmentedGroup;


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
    EasyPopup popup_window;

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
                popup_window = new EasyPopup(getContext())
                        .setContentView(R.layout.popup_layout)
                        .setFocusAndOutsideEnable(true)
                        .setBackgroundDimEnable(true)
                        .setDimValue(0.3f)
                        .createPopup();
                popup_window.showAtAnchorView(menuItemView, VerticalGravity.BELOW,
                        HorizontalGravity.ALIGN_RIGHT);


                RadioButton rbtn_title = popup_window.getView(R.id.rbtn_title);
                RadioButton rbtn_price = popup_window.getView(R.id.rbtn_price);
                RadioButton rbtn_market_cap = popup_window.getView(R.id.rbtn_market_cap);
                RadioButton rbtn_growth_percent = popup_window.getView(R.id.rbtn_growth_percent);

                SegmentedGroup segmented_btn_price_change = popup_window.
                        getView(R.id.segmented_btn_price_change);
                RadioButton btn_hour = popup_window.getView(R.id.btn_hour);
                RadioButton btn_day = popup_window.getView(R.id.btn_day);
                RadioButton btn_week = popup_window.getView(R.id.btn_week);

                ImageButton imagebutton_up = popup_window.getView(R.id.imagebutton_up);
                ImageButton imagebutton_down = popup_window.getView(R.id.imagebutton_down);
                Button btn_ok = popup_window.getView(R.id.btn_ok);


                rbtn_title.setOnClickListener(v -> {
                    segmented_btn_price_change.setTintColor(
                            getResources().getColor(R.color.colorPopupBtnUnselected));
                });

                rbtn_price.setOnClickListener(v -> {
                    segmented_btn_price_change.setTintColor(
                            getResources().getColor(R.color.colorPopupBtnUnselected));
                });

                rbtn_market_cap.setOnClickListener(v -> {
                    segmented_btn_price_change.setTintColor(
                            getResources().getColor(R.color.colorPopupBtnUnselected));
                });

                rbtn_growth_percent.setOnClickListener(v -> {
                    segmented_btn_price_change.setTintColor(
                            getResources().getColor(R.color.colorPopupSelectedSegmentedBtn));
                });


                btn_hour.setOnClickListener(v -> {
                    rbtn_growth_percent.setChecked(true);
                    segmented_btn_price_change.setTintColor(
                            getResources().getColor(R.color.colorPopupSelectedSegmentedBtn));
                });

                btn_day.setOnClickListener(v -> {
                    rbtn_growth_percent.setChecked(true);
                    segmented_btn_price_change.setTintColor(
                            getResources().getColor(R.color.colorPopupSelectedSegmentedBtn));
                });

                btn_week.setOnClickListener(v -> {
                    rbtn_growth_percent.setChecked(true);
                    segmented_btn_price_change.setTintColor(
                            getResources().getColor(R.color.colorPopupSelectedSegmentedBtn));
                });

                imagebutton_up.setOnClickListener(v -> {

                    Drawable bg = DrawableCompat.wrap(imagebutton_up.getBackground());
                    DrawableCompat.setTint(bg, TickerFragment.this.getResources().getColor(R.color.colorPopupBtnSelected));

                    bg = DrawableCompat.wrap(imagebutton_down.getBackground());
                    DrawableCompat.setTint(bg, TickerFragment.this.getResources().getColor(R.color.colorPopupBtnUnselected));

                });

                imagebutton_down.setOnClickListener(v -> {

                    Drawable bg = DrawableCompat.wrap(imagebutton_down.getBackground());
                    DrawableCompat.setTint(bg, getResources().getColor(R.color.colorPopupBtnSelected));

                    bg = DrawableCompat.wrap(imagebutton_up.getBackground());
                    DrawableCompat.setTint(bg, getResources().getColor(R.color.colorPopupBtnUnselected));

                });


                btn_ok.setOnClickListener(v -> {
                    //todo sorting

                    popup_window.dismiss();
                });

                rbtn_title.setChecked(true);


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

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null)
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_cards);
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

            CreateTickerDialog dialog = CreateTickerDialog.newInstance();
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

        //todo sort here

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
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

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
}
