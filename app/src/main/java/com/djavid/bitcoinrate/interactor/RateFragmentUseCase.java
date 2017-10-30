package com.djavid.bitcoinrate.interactor;


import com.djavid.bitcoinrate.model.DataRepository;
import com.djavid.bitcoinrate.model.RestDataRepository;
import com.djavid.bitcoinrate.model.dto.BlockchainModel;
import com.djavid.bitcoinrate.model.dto.CryptonatorTicker;
import com.djavid.bitcoinrate.model.dto.CurrenciesModel;
import com.djavid.bitcoinrate.model.dto.HistoryDataModel;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;


public class RateFragmentUseCase implements RateFragmentInteractor {

    private DataRepository dataRepository;

    public RateFragmentUseCase(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public RateFragmentUseCase() {
        this(new RestDataRepository());
    }

    @Override
    public Single<CryptonatorTicker> getRate(String curr1, String curr2) {
        return dataRepository.getRate(curr1, curr2)
                .doOnError(Throwable::printStackTrace);
    }

    @Override
    public Single<CurrenciesModel> getCurrencies() {
        return dataRepository.getCurrencies()
                .doOnError(Throwable::printStackTrace);
    }

    @Override
    public Single<BlockchainModel> getChartValues(String timespan, boolean sampled, String format) {
        return dataRepository.getChartValues(timespan, sampled, format)
                .doOnError(Throwable::printStackTrace);
    }

    @Override
    public Single<HistoryDataModel> getHistory(String curr, int periods, long after) {
        return dataRepository.getHistory(curr, periods, after)
                .doOnError(Throwable::printStackTrace);
    }

}
