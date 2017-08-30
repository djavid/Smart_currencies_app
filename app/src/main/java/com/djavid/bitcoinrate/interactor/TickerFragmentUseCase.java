package com.djavid.bitcoinrate.interactor;

import com.djavid.bitcoinrate.model.DataRepository;
import com.djavid.bitcoinrate.model.RestDataRepository;


public class TickerFragmentUseCase implements TickerFragmentInteractor {

    private DataRepository dataRepository;

    public TickerFragmentUseCase(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public TickerFragmentUseCase() {
        this(new RestDataRepository());
    }


}
