package com.djavid.bitcoinrate.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.adapter.TickerItem;
import com.djavid.bitcoinrate.core.BaseFragment;
import com.djavid.bitcoinrate.domain.MainRouter;
import com.djavid.bitcoinrate.model.dto.LabelItemDto;
import com.djavid.bitcoinrate.model.realm.TickerItemRealm;
import com.djavid.bitcoinrate.presenter.interfaces.TickerFragmentPresenter;
import com.djavid.bitcoinrate.view.interfaces.TickerFragmentView;
import com.mindorks.placeholderview.PlaceHolderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.realm.Realm;


public class TickerFragment extends BaseFragment implements TickerFragmentView {

    @BindView(R.id.rv_ticker_list)
    PlaceHolderView rv_ticker_list;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    TickerFragmentPresenter presenter;
    private OnTickerInteractionListener mTickerListener;
    Realm realm = Realm.getDefaultInstance();


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

        super.onStart();
    }

    @Override
    public void onStop() {
        presenter.setView(null);
        rv_ticker_list.removeAllViews(); //TODO
        super.onStop();
    }

    @Override
    public View setupView(View view) {
        //setupTickerRecyclerView();

        fab.setOnClickListener(v -> {
            //TickerItemRealm tickerItem = realm.createObject(TickerItemRealm.class);
        });

        return view;
    }

    @Override
    public void loadData() {
        List<LabelItemDto> labelItems = new ArrayList<>();
        labelItems.add(new LabelItemDto("2450", true));
        labelItems.add(new LabelItemDto("2100", false));

        //TickerItemRealm tickerItem = realm.createObject(TickerItemRealm.class);
        //TickerItemRealm.load()

        //TickerItemRealm.

        rv_ticker_list.addView(new TickerItem(getContext(), rv_ticker_list,
                new TickerItemRealm("2403", "BTC", "USD", labelItems)));
        rv_ticker_list.addView(new TickerItem(getContext(), rv_ticker_list,
                new TickerItemRealm("17 553,74", "ETH", "RUB")));
        rv_ticker_list.addView(new TickerItem(getContext(), rv_ticker_list,
                new TickerItemRealm("46,02", "LTC", "EUR", labelItems)));
        rv_ticker_list.addView(new TickerItem(getContext(), rv_ticker_list,
                new TickerItemRealm("72,95", "NVC", "UAH", labelItems)));
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

}
