package com.djavid.bitcoinrate.interactor;


import com.djavid.bitcoinrate.model.DataRepository;
import com.djavid.bitcoinrate.model.RestDataRepository;
import com.djavid.bitcoinrate.model.dto.blockchain.BlockchainModel;
import com.djavid.bitcoinrate.model.dto.coinmarketcap.CoinMarketCapTicker;
import com.djavid.bitcoinrate.model.dto.cryptonator.CryptonatorTicker;
import com.djavid.bitcoinrate.model.dto.cryptonator.CurrenciesModel;
import com.djavid.bitcoinrate.model.dto.cryptowatch.HistoryDataModel;
import com.djavid.bitcoinrate.model.dto.heroku.ResponseId;

import java.util.List;

import io.reactivex.Single;


public class RateFragmentUseCase implements RateFragmentInteractor {

    private DataRepository dataRepository;

    private RateFragmentUseCase(DataRepository dataRepository) {
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

    @Override
    public Single<List<CoinMarketCapTicker>> getRateCMC(String crypto_id, String country_id) {
        return dataRepository.getRateCMC(crypto_id, country_id)
                .doOnError(Throwable::printStackTrace);
    }

    @Override
    public Single<ResponseId> registerToken(String token, long id) {
        return dataRepository.registerToken(token, id)
                .doOnError(Throwable::printStackTrace);
    }
}
