package com.djavid.bitcoinrate.model;


import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.model.dto.BlockchainModel;
import com.djavid.bitcoinrate.model.dto.CryptonatorTicker;
import com.djavid.bitcoinrate.model.dto.CurrenciesModel;
import com.djavid.bitcoinrate.model.dto.HistoryDataModel;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;


public class RestDataRepository implements DataRepository {

    private ApiInterface apiInterface;

    public RestDataRepository(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public RestDataRepository() {
        this(App.getAppInstance().getApiInterface());
    }

    @Override
    public Single<CryptonatorTicker> getRate(String curr1, String curr2) {
        return apiInterface.getRate(curr1, curr2);
    }

    @Override
    public Single<CurrenciesModel> getCurrencies() {
        return apiInterface.getCurrencies();
    }

    @Override
    public Single<BlockchainModel> getChartValues(String timespan, boolean sampled, String format) {
        return apiInterface.getChartValues(timespan, sampled, format);
    }

    @Override
    public Single<HistoryDataModel> getHistory(String curr, int periods, long after) {
        return apiInterface.getHistory(curr, periods, after);
    }
}
