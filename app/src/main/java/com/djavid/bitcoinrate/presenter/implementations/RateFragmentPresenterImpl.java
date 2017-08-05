package com.djavid.bitcoinrate.presenter.implementations;

import com.djavid.bitcoinrate.core.BasePresenter;
import com.djavid.bitcoinrate.domain.MainRouter;
import com.djavid.bitcoinrate.presenter.interfaces.RateFragmentPresenter;
import com.djavid.bitcoinrate.view.interfaces.RateFragmentView;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

/**
 * Created by djavid on 05.08.17.
 */


public class RateFragmentPresenterImpl extends BasePresenter<RateFragmentView, MainRouter, Object>
        implements RateFragmentPresenter {

    private Disposable disposable = Disposables.empty();


    public RateFragmentPresenterImpl() { }

    @Override
    public String getId() {
        return "rate_fragment";
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void saveInstanceState(Object instanceState) {
        setInstanceState(instanceState);
    }
}
