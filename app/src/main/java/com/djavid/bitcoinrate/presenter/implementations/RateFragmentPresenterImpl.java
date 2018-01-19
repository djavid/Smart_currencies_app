package com.djavid.bitcoinrate.presenter.implementations;

import android.util.Log;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.core.BasePresenter;
import com.djavid.bitcoinrate.core.Router;
import com.djavid.bitcoinrate.model.RestDataRepository;
import com.djavid.bitcoinrate.model.dto.coinmarketcap.CoinMarketCapTicker;
import com.djavid.bitcoinrate.model.dto.cryptocompare.Datum;
import com.djavid.bitcoinrate.model.dto.cryptocompare.HistoData;
import com.djavid.bitcoinrate.model.dto.realm.RealmHistoryData;
import com.djavid.bitcoinrate.presenter.instancestate.RateFragmentInstanceState;
import com.djavid.bitcoinrate.presenter.interfaces.RateFragmentPresenter;
import com.djavid.bitcoinrate.util.Codes;
import com.djavid.bitcoinrate.util.DateFormatter;
import com.djavid.bitcoinrate.view.interfaces.RateFragmentView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;
import io.realm.Realm;
import io.realm.RealmList;


public class RateFragmentPresenterImpl extends BasePresenter<RateFragmentView, Router, RateFragmentInstanceState>
        implements RateFragmentPresenter {

    private final String TAG = this.getClass().getSimpleName();
    private Disposable disposable = Disposables.empty();
    private RestDataRepository dataRepository;


    public RateFragmentPresenterImpl() {
        dataRepository = new RestDataRepository();
    }

    @Override
    public String getId() {
        return "rate_fragment";
    }

    @Override
    public void onStart() {

        if (getInstanceState() != null && getView() != null) {

            getView().setAllChartLabelsUnselected();
            getView().setSelectedChartOption(getInstanceState().getChart_option());

            if (getView().getSelectedChartLabelView() != null)
                getView().setChartLabelSelected(getView().getSelectedChartLabelView());
        }

        if (getView() != null) {

            //getView().setCurrenciesSpinner();

            if (!App.getAppInstance().getPreferences().getPrice().isEmpty())
                getView().getTopPanel().setText(App.getAppInstance().getPreferences().getPrice());

            String curr1 = Codes.getCryptoCurrencySymbol(getView().getLeftSpinner().getSelectedItem().toString());
            String curr2 = getView().getRightSpinner().getSelectedItem().toString();
            String pair = curr1 + curr2;

            final RealmHistoryData realmFound = getValuesFromRealm();
            if (realmFound != null) {
                String realm_pair = realmFound.getPair();

                if (realm_pair.equals(pair))
                    loadValuesToChart(realmFound.getValues());
                else clearValuesFromRealm();
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

    @Override
    public void refresh() {
        showRate(true);
    }

    @Override
    public void showRate(boolean refresh) {
        showRateCMC(refresh);
    }

    @Override
    public void showRateCryptonator(boolean refresh) {
        Log.i(TAG, "showRateCryptonator("+ refresh+ ")");
        if (refresh) setRefreshing(true);

        final String curr1 = Codes.getCryptoCurrencySymbol(getView().getLeftSpinner().getSelectedItem().toString());
        final String curr2 = getView().getRightSpinner().getSelectedItem().toString();

        disposable = dataRepository.getRate(curr1, curr2)
                .subscribe(ticker -> {

                    if (!ticker.getError().isEmpty()) {
                        if (getView() != null)
                            getView().showError(R.string.server_error);
                        Log.e(TAG, ticker.getError());

                        return;
                    }

                    double price = ticker.getTicker().getPrice();
                    String text = DateFormatter.convertPrice(price) + " " + ticker.getTicker().getTarget();

                    if (getView() != null) {
                        if (!getView().getTopPanel().getText().equals(text)) {
                            getView().getTopPanel().setText(text);
                            App.getAppInstance().getPreferences().setPrice(text);
                        }

                        Codes.ChartOption chartOption = getView().getSelectedChartOption();
                        showChart(curr1, curr2, chartOption, refresh);
                    }

                }, error -> {
                    if (getView() != null) getView().showError(R.string.connection_error);
                    setRefreshing(false);
                });
    }

    @Override
    public void showRateCMC(boolean refresh) {
        Log.i(TAG, "showRateCMC("+ refresh+ ")");
        if (getView() == null) return;

        if (refresh) setRefreshing(true);

        final String crypto_id = Codes.getCryptoCurrencySymbol(
                getView().getLeftSpinner().getSelectedItem().toString());
        final String crypto_full_id = Codes.getCryptoCurrencyId(crypto_id);
        final String country_id = getView().getRightSpinner().getSelectedItem().toString();

        disposable = dataRepository.getRateCMC(crypto_full_id, country_id)
                .subscribe(array -> {

                    CoinMarketCapTicker ticker = array.get(0);

                    double price = ticker.getPrice(country_id);
                    String text = DateFormatter.convertPrice(price) + " " + country_id;

                    if (getView() != null) {
                        if (!getView().getTopPanel().getText().equals(text)) {
                            getView().getTopPanel().setText(text);
                            App.getAppInstance().getPreferences().setPrice(text);
                        }

                        Codes.ChartOption chartOption = getView().getSelectedChartOption();
                        showChart(crypto_id, country_id, chartOption, refresh);
                    }

                }, error -> {
                    //if (getView() != null) getView().showError(R.string.connection_error);
                    showRateCryptonator(refresh);
                    setRefreshing(false);
                });
    }

    @Override
    public void showChart(String crypto, String country, Codes.ChartOption chartOption, boolean refresh) {
        Log.i(TAG, "showChart()");

        getHistory(Codes.getCryptoCurrencySymbol(crypto), country, chartOption);
    }

    private void getHistory(String from_symbol, String to_symbol, Codes.ChartOption chartOption) {
        Log.i(TAG, "getHistoryCC()");

        Consumer<HistoData> onSubscribe = result -> {

            if (result == null || result.response.equals("Error") ||
                    result.getData() == null || result.getData().isEmpty()) {
                setRefreshing(false);
                return;
            }

            if (!valuesEqualWithRealm(result.getData())) {
                saveValuesToRealm(result.getData(), from_symbol + to_symbol);
                loadValuesToChart(result.getData());
            }

            setRefreshing(false);

        };

        Consumer<Throwable> onError = error -> {

            if (getView() != null) {
                getView().getRateChart().getChart().clear();
                //getView().showError(R.string.server_error);
            }
            setRefreshing(false);

        };

        switch (chartOption.request) {
            case "histominute":

                disposable = dataRepository.getHistoMinute(from_symbol, to_symbol,
                        chartOption.limit, chartOption.periods).subscribe(onSubscribe, onError);
                break;

            case "histohour":

                disposable = dataRepository.getHistoHour(from_symbol, to_symbol,
                        chartOption.limit, chartOption.periods).subscribe(onSubscribe, onError);
                break;

            case "histoday":

                disposable = dataRepository.getHistoDay(from_symbol, to_symbol,
                        chartOption.limit, chartOption.periods).subscribe(onSubscribe, onError);
                break;
        }

    }


    private boolean valuesEqualWithRealm(List<Datum> values) {
        Log.i(TAG, "valuesEqualWithRealm()");

        final RealmHistoryData realmFound = getValuesFromRealm();

        if (realmFound != null) {
            List<Datum> savedValues = realmFound.getValues();

            if (values.size() == savedValues.size()) {
                for (int i = 0; i < values.size(); i++) {
                    if (!values.get(i).equals(savedValues.get(i))) {
                        Log.i(TAG, "Values not equal!");
                        return false;
                    }
                }

                Log.i(TAG, "Values equal!");
                return true;
            }
        }

        Log.i(TAG, "Values not equal!");
        return false;
    }

    private RealmHistoryData getValuesFromRealm() {
        Log.i(TAG, "getValuesFromRealm()");

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final RealmHistoryData realmFound =
                realm.where(RealmHistoryData.class).findFirst();
        realm.commitTransaction();

        return realmFound;
    }

    private void saveValuesToRealm(List<Datum> values, String pair) {
        Log.i(TAG, "saveValuesToRealm()");

        RealmList<Datum> realmList = new RealmList<>();
        realmList.addAll(values);
        RealmHistoryData realmHistoryData = new RealmHistoryData(realmList, pair);

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(RealmHistoryData.class).findAll().deleteAllFromRealm();
        realm.copyToRealm(realmHistoryData);
        realm.commitTransaction();
    }

    private void clearValuesFromRealm() {
        Log.i(TAG, "clearValuesFromRealm()");

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(RealmHistoryData.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }


    private void loadValuesToChart(List<Datum> values) {
        Log.i(TAG, "loadValuesToChart()");


        List<Entry> lineEntries = new ArrayList<>();
        List<CandleEntry> candleEntries = new ArrayList<>();
        List<Long> dates = new ArrayList<>();

        int j = 0;
        for (int i = 0; i < values.size(); i++) {

            long date = values.get(i).getTime();

            float open = values.get(i).getOpen().floatValue();
            float close = values.get(i).getClose().floatValue();

            float high = values.get(i).getHigh().floatValue();
            float low = values.get(i).getLow().floatValue();

            float volume = values.get(i).getVolumefrom().floatValue() / 10;

            if (open != 0 && close != 0 && high != 0 && low != 0) {
                dates.add(date);
                candleEntries.add(new CandleEntry(j, high, low, open, close));
                lineEntries.add(new Entry(j, volume));
                j++;
            }
        }

        if (getView() != null)
            getView().getRateChart().setData(candleEntries, lineEntries, dates);
    }

    private void setRefreshing(boolean key) {
//        if (getView() != null)
//            if (getView().getRefreshLayout() != null)
//                getView().getRefreshLayout().setRefreshing(key);
    }

}
