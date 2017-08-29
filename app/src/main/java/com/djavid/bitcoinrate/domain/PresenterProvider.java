package com.djavid.bitcoinrate.domain;

import com.djavid.bitcoinrate.core.Presenter;
import com.djavid.bitcoinrate.presenter.implementations.RateFragmentPresenterImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by djavid on 05.08.17.
 */


public class PresenterProvider {

    private Map<String, Presenter> presenterMap;

    public PresenterProvider() {
        presenterMap = new HashMap<>();
    }

    public <T extends Presenter> T getPresenter(String presenterId, Class<T> c) {
        createPresenter(presenterId);
        return c.cast(presenterMap.get(presenterId));
    }

    private void createPresenter(String presenterId) {
        if (presenterMap.containsKey(presenterId)) return;

        switch (presenterId){
            case "rate_fragment":
                presenterMap.put(presenterId, new RateFragmentPresenterImpl());
                break;
            case "ticker_fragment":

                break;
        }
    }

    public void removePresenter(String presenterId) {
        if (presenterMap.containsKey(presenterId))
            presenterMap.remove(presenterId);
    }

}
