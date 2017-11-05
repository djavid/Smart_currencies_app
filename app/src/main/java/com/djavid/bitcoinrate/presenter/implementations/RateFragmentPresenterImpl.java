package com.djavid.bitcoinrate.presenter.implementations;

import android.util.Log;
import android.view.View;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.core.BasePresenter;
import com.djavid.bitcoinrate.domain.MainRouter;
import com.djavid.bitcoinrate.interactor.RateFragmentInteractor;
import com.djavid.bitcoinrate.interactor.RateFragmentUseCase;
import com.djavid.bitcoinrate.model.dto.HistoryDataModel;
import com.djavid.bitcoinrate.model.dto.Value;
import com.djavid.bitcoinrate.presenter.interfaces.RateFragmentPresenter;
import com.djavid.bitcoinrate.util.DateFormatter;
import com.djavid.bitcoinrate.util.RxUtils;
import com.djavid.bitcoinrate.view.interfaces.RateFragmentView;
import com.github.mikephil.charting.data.Entry;

import org.joda.time.LocalDateTime;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;


public class RateFragmentPresenterImpl extends BasePresenter<RateFragmentView, MainRouter, Object>
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

    }

    @Override
    public void onStop() {
        disposable.dispose();
    }

    @Override
    public void saveInstanceState(Object instanceState) {
        setInstanceState(instanceState);
    }

    private void setRefreshing(boolean key) {
        if (getView() != null) {
            if (getView().getRefreshLayout() != null)
                getView().getRefreshLayout().setRefreshing(key);
        }
    }

    @Override
    public void refresh() {
        showRate(true);
    }

    @Override
    public void showRate(boolean update_chart) {
        setRefreshing(true);

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
                    String text = DateFormatter.convertPrice(price, ticker) + " " + ticker.getTicker().getTarget();

                    if (getView() != null) {
                        if (!getView().getTopPanel().getText().equals(text)) {
                            getView().getTopPanel().setText(text);
                        }

                        //if (update_chart) showChart(getView().getSelectedTimespan());

                        int daysAgo = getView().getTimespanDays();
                        long end = LocalDateTime.now().withHourOfDay(3).plusDays(1).toDateTime().getMillis() / 1000;
                        long start = end - 86400 * daysAgo;

                        if (update_chart) getHistory(curr1 + curr2, 86400, start);
                        else setRefreshing(false);
                    }

                }, error -> {
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

    @Override
    public void getHistory(String curr, int periods, long after) {
        setRefreshing(true);

        disposable = rateFragmentInteractor.getHistory(curr, periods, after)
                .compose(RxUtils.applySingleSchedulers())
                .retry(2L)
                .subscribe(result -> {
                    List<List<Double>> values = result.getResult().getValues();

                    for (List<Double> item : values) {
                        Double weightened = (item.get(1) + item.get(2) + item.get(3) + item.get(4)) / 4;
                        System.out.println(new Timestamp(item.get(0).longValue() * 1000).toString() + " " + weightened);
                    }

                    List<Entry> entries = new ArrayList<Entry>();
                    int color = App.getContext().getResources().getColor(R.color.colorChart);

                    for (int i = 0; i < values.size(); i++) {
                        //Date x = new Date(tick.getLong("x") * 1000);
                        long X = values.get(i).get(0).longValue() * 1000;
                        double y = (values.get(i).get(1) + values.get(i).get(2) +
                                values.get(i).get(3) + values.get(i).get(4)) / 4;

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



}
