package com.djavid.bitcoinrate.presenter.implementations;

import android.util.Log;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.core.BasePresenter;
import com.djavid.bitcoinrate.domain.MainRouter;
import com.djavid.bitcoinrate.interactor.RateFragmentInteractor;
import com.djavid.bitcoinrate.interactor.RateFragmentUseCase;
import com.djavid.bitcoinrate.model.dto.coinmarketcap.CoinMarketCapTicker;
import com.djavid.bitcoinrate.model.dto.blockchain.Value;
import com.djavid.bitcoinrate.model.dto.cryptowatch.HistoryDataModel;
import com.djavid.bitcoinrate.presenter.instancestate.RateFragmentInstanceState;
import com.djavid.bitcoinrate.presenter.interfaces.RateFragmentPresenter;
import com.djavid.bitcoinrate.util.Codes;
import com.djavid.bitcoinrate.util.DateFormatter;
import com.djavid.bitcoinrate.util.RxUtils;
import com.djavid.bitcoinrate.view.interfaces.RateFragmentView;
import com.github.mikephil.charting.data.Entry;
import com.google.firebase.iid.FirebaseInstanceId;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;


public class RateFragmentPresenterImpl extends BasePresenter<RateFragmentView, MainRouter, RateFragmentInstanceState>
        implements RateFragmentPresenter {

    private Disposable disposable = Disposables.empty();
    private RateFragmentInteractor rateFragmentInteractor;


    public RateFragmentPresenterImpl() {
        rateFragmentInteractor = new RateFragmentUseCase();
    }


    @Override
    public String getId() {
        return "rate_fragment";
    }

    @Override
    public void onStart() {
        if (getInstanceState() != null) {
            if (getView() != null) {
                getView().setChartLabelSelected(getInstanceState().getTimespan());
            }
        }
    }

    @Override
    public void onStop() {
        disposable.dispose();
    }

    @Override
    public void saveInstanceState(RateFragmentInstanceState instanceState) {
        setInstanceState(instanceState);
    }

    private void setRefreshing(boolean key) {
        if (getView() != null)
            if (getView().getRefreshLayout() != null)
                getView().getRefreshLayout().setRefreshing(key);
    }

    @Override
    public void refresh() {
        showRate(true);
    }

    @Override
    public void showRate(boolean update_chart) {
        boolean refresh = true;

        if (getInstanceState() != null) {
            refresh = false;
        }

        showRateCMC(update_chart, refresh);
    }

    @Override
    public void showRateCryptonator(boolean update_chart, boolean refresh) {
        if (refresh) setRefreshing(true);

        final String curr1 = getView().getLeftSpinner().getSelectedItem().toString();
        final String curr2 = getView().getRightSpinner().getSelectedItem().toString();

        disposable = rateFragmentInteractor.getRate(curr1, curr2)
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L)
                .subscribe(ticker -> {
                    if (!ticker.getError().isEmpty()) {
                        //TODO
                        Log.e("showRate():", ticker.getError());
                        return;
                    }

                    double price = ticker.getTicker().getPrice();
                    String text = DateFormatter.convertPrice(price) + " " + ticker.getTicker().getTarget();

                    if (getView() != null) {
                        if (!getView().getTopPanel().getText().equals(text)) {
                            getView().getTopPanel().setText(text);
                        }

                        int daysAgo = getView().getTimespanDays();
                        long end = LocalDateTime.now().withHourOfDay(3).plusDays(1).toDateTime().getMillis() / 1000;
                        long start = end - 86400 * daysAgo;

                        if (update_chart) getHistory(curr1 + curr2, daysAgo, start, refresh);
                        else setRefreshing(false);
                    }

                }, error -> {
                    setRefreshing(false);
                });
    }

    @Override
    public void showRateCMC(boolean update_chart, boolean refresh) {
        if (getView() == null) return;

        if (refresh) setRefreshing(true);

        final String crypto_id = getView().getLeftSpinner().getSelectedItem().toString();
        final String crypto_full_id = Codes.getCryptoCurrencyId(crypto_id);
        final String country_id = getView().getRightSpinner().getSelectedItem().toString();

        disposable = rateFragmentInteractor.getRateCMC(crypto_full_id, country_id)
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L)
                .subscribe(array -> {

                    CoinMarketCapTicker ticker = array.get(0);

                    double price = ticker.getPrice(country_id);
                    String text = DateFormatter.convertPrice(price) + " " + country_id;

                    if (getView() != null) {
                        if (!getView().getTopPanel().getText().equals(text)) {
                            getView().getTopPanel().setText(text);
                        }

                        int daysAgo = getView().getTimespanDays();
                        long end = LocalDateTime.now().withHourOfDay(3).plusDays(1).toDateTime().getMillis() / 1000;
                        long start = end - 86400 * daysAgo;

                        if (update_chart) getHistory(crypto_id + country_id, daysAgo, start, refresh);
                        else setRefreshing(false);
                    }

                }, error -> {
                    setRefreshing(false);
                });
    }

    @Override
    public void getHistory(String curr, int timespanDays, long after, boolean refresh) {
        if (refresh) setRefreshing(true);

        int periods;
        switch (timespanDays) {
            case 30:
                periods = 7200;
                break;
            case 90:
                periods = 21600;
                break;
            case 180:
                periods = 43200;
                break;
            case 365:
                periods = 86400;
                break;
            default:
                periods = 86400;
                break;
        }

        disposable = rateFragmentInteractor.getHistory(curr, periods, after)
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L)
                .subscribe(result -> {
                    List<List<Double>> values = result.getResult().getValues();
                    if (values.size() == 0) {
                        getHistory(curr, periods, after, refresh);
                        return;
                    }

                    List<Entry> entries = new ArrayList<>();
                    for (int i = 0; i < values.size(); i++) {
                        //Date x = new Date(tick.getLong("x") * 1000);
                        long X = values.get(i).get(0).longValue() * 1000;
                        double y = (values.get(i).get(1) + values.get(i).get(2) +
                                values.get(i).get(3) + values.get(i).get(4)) / 4;

                        entries.add(new Entry(X, (float) y));
                    }

                    if (getView() != null) {
                        int color = App.getContext().getResources().getColor(R.color.colorChart);
                        getView().getRateChart().initialize(entries, color);
                    }

                    setRefreshing(false);

                }, error -> {
                    if (getView() != null)
                        getView().getRateChart().getChart().clear();
                    setRefreshing(false);
                });
    }

    @Override
    public void showChart(String timespan) {
        setRefreshing(true);

        disposable = rateFragmentInteractor.getChartValues(timespan, true, "json")
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L)
                .subscribe(response -> {
                    List<Entry> entries = new ArrayList<Entry>();
                    List<Value> values = response.getValues();
                    int color = App.getContext().getResources().getColor(R.color.colorChart);

                    for (int i = 0; i < values.size(); i++) {
                        //Date x = new Date(tick.getLong("x") * 1000);
                        long X = values.get(i).getX() * 1000;
                        double y = values.get(i).getY();

                        entries.add(new Entry(X, (float)y));
                    }

                    if (getView() != null) {
                        getView().getRateChart().initialize(entries, color);
                        setRefreshing(false);
                    }

                }, error -> {
                    setRefreshing(false);
                });
    }

//    public void sendTokenToServer() {
//
//        String token = FirebaseInstanceId.getInstance().getToken();
//        if (token == null || token.isEmpty()) {
//            return;
//        }
//
//        long id;
//        if (App.getAppInstance().getPrefencesWrapper().sharedPreferences.contains("token_id")) {
//            id = App.getAppInstance().getPrefencesWrapper().sharedPreferences.getLong("token_id", 0);
//        } else {
//            id = 0;
//        }
//
//        disposable = rateFragmentInteractor.registerToken(token, id)
//                .compose(RxUtils.applySingleSchedulers())
//                .subscribe(response -> {
//
//                    if (response.error.isEmpty()) {
//
//                        if (response.id != 0) {
//
//                            App.getAppInstance()
//                                    .getPrefencesWrapper()
//                                    .sharedPreferences
//                                    .edit()
//                                    .putLong("token_id", response.id)
//                                    .apply();
//
//                            App.getAppInstance()
//                                    .getPrefencesWrapper()
//                                    .sharedPreferences
//                                    .edit()
//                                    .putString("token", token)
//                                    .apply();
//                        }
//                    }
//                }, error -> {
//
//                });
//    }

}
