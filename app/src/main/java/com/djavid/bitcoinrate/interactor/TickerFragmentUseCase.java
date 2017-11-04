package com.djavid.bitcoinrate.interactor;

import com.djavid.bitcoinrate.model.DataRepository;
import com.djavid.bitcoinrate.model.RestDataRepository;
import com.djavid.bitcoinrate.model.dto.CryptonatorTicker;
import com.djavid.bitcoinrate.model.dto.HistoryDataModel;

import io.reactivex.Single;


public class TickerFragmentUseCase implements TickerFragmentInteractor {

    private DataRepository dataRepository;

    public TickerFragmentUseCase(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public TickerFragmentUseCase() {
        this(new RestDataRepository());
    }

    @Override
    public Single<CryptonatorTicker> getRate(String curr1, String curr2) {
        return dataRepository.getRate(curr1, curr2)
                .doOnError(Throwable::printStackTrace);
    }
}
