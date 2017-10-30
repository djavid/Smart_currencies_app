package com.djavid.bitcoinrate.presenter.implementations;

import com.djavid.bitcoinrate.core.BasePresenter;
import com.djavid.bitcoinrate.domain.MainRouter;
import com.djavid.bitcoinrate.interactor.TickerFragmentInteractor;
import com.djavid.bitcoinrate.interactor.TickerFragmentUseCase;
import com.djavid.bitcoinrate.presenter.interfaces.TickerFragmentPresenter;
import com.djavid.bitcoinrate.util.RxUtils;
import com.djavid.bitcoinrate.view.interfaces.TickerFragmentView;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;


public class TickerFragmentPresenterImpl extends BasePresenter<TickerFragmentView, MainRouter, Object>
        implements TickerFragmentPresenter {

    private Disposable disposable = Disposables.empty();
    private TickerFragmentInteractor tickerFragmentInteractor;

    @Override
    public String getId() {
        return "ticker_fragment";
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        disposable.dispose();
    }

    @Override
    public void saveInstanceState(Object instanceState) {
        setInstanceState(instanceState);
    }

    public TickerFragmentPresenterImpl() {
        tickerFragmentInteractor = new TickerFragmentUseCase();
    }

}
