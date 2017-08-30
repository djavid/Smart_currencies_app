package com.djavid.bitcoinrate.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.adapter.TickerRecyclerAdapter;
import com.djavid.bitcoinrate.core.BaseFragment;
import com.djavid.bitcoinrate.domain.MainRouter;
import com.djavid.bitcoinrate.model.realm.TickerItem;
import com.djavid.bitcoinrate.presenter.interfaces.TickerFragmentPresenter;
import com.djavid.bitcoinrate.view.interfaces.TickerFragmentView;

import java.util.List;

import butterknife.BindView;
import io.realm.Realm;


public class TickerFragment extends BaseFragment implements TickerFragmentView {

    @BindView(R.id.rv_ticker_list)
    RecyclerView rv_ticker_list;

    TickerFragmentPresenter presenter;
    private OnFragmentInteractionListener mListener;
    TickerRecyclerAdapter recyclerAdapter;
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
        super.onStop();
    }

    @Override
    public View setupView(View view) {
        setupRecyclerView();

        return view;
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(TickerItem item);
    }

    private void setupRecyclerView() {
        recyclerAdapter = new TickerRecyclerAdapter(getContext(), mListener);
        rv_ticker_list.setAdapter(recyclerAdapter);
        rv_ticker_list.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void scrollToPosition(int position) {
        rv_ticker_list.scrollToPosition(position);
    }

    @Override
    public void appendFeed(List<TickerItem> feed) {
        recyclerAdapter.appendDataWithNotify(feed);
    }

    @Override
    public void resetFeed() {
        recyclerAdapter.clear();
    }
}
