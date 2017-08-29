package com.djavid.bitcoinrate.interactor;


import com.djavid.bitcoinrate.model.dto.BlockchainModel;
import com.djavid.bitcoinrate.model.dto.CryptonatorTicker;
import com.djavid.bitcoinrate.model.dto.CurrenciesModel;

import io.reactivex.Single;
import retrofit2.Call;


public interface RateFragmentInteractor {
    Single<CryptonatorTicker> getRate(String curr1, String curr2);
    Single<CurrenciesModel> getCurrencies();
    Single<BlockchainModel> getChartValues(String timespan, boolean sampled, String format);
}
