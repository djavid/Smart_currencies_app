package com.djavid.bitcoinrate.interactor;

import com.djavid.bitcoinrate.model.dto.CryptonatorTicker;
import com.djavid.bitcoinrate.model.dto.HistoryDataModel;
import io.reactivex.Single;


public interface TickerFragmentInteractor {
    Single<CryptonatorTicker> getRate(String curr1, String curr2);
}
