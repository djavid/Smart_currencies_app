package com.djavid.bitcoinrate.presenter.implementations;

import android.util.Log;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.core.BasePresenter;
import com.djavid.bitcoinrate.core.Router;
import com.djavid.bitcoinrate.model.coinmarketcap.CoinMarketCapTicker;
import com.djavid.bitcoinrate.model.cryptocompare.Datum;
import com.djavid.bitcoinrate.model.cryptocompare.HistoData;
import com.djavid.bitcoinrate.model.project.ChartOption;
import com.djavid.bitcoinrate.model.realm.RealmHistoryData;
import com.djavid.bitcoinrate.presenter.instancestate.RateFragmentInstanceState;
import com.djavid.bitcoinrate.presenter.interfaces.RateFragmentPresenter;
import com.djavid.bitcoinrate.rest.RestDataRepository;
import com.djavid.bitcoinrate.util.PriceConverter;
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


    @Override
    public void onStart() {

        if (getView() != null) {

            //restore clicked chart option
            if (getInstanceState() != null) {
                getView().setSelectedChartOption(getInstanceState().getChart_option());
                if (getView().getSelectedChartLabelView() != null)
                    getView().setChartLabelSelected(getView().getSelectedChartLabelView());
            }

            //restore last price
            if (!App.getAppInstance().getPreferences().getPrice().isEmpty())
                getView().getTopPanel().setText(App.getAppInstance().getPreferences().getPrice());

            //restore history data from realm
            final RealmHistoryData realmHistoryData = getValuesFromRealm();
            if (realmHistoryData != null) {

                String crypto_symbol = getView().getCrypto_selected().symbol;
                String country_symbol = getView().getCountry_selected().symbol;
                String pair = crypto_symbol + country_symbol;
                System.out.println(pair);
                System.out.println(realmHistoryData.getPair());
                ChartOption option = getView().getSelectedChartOption();

                if (realmHistoryData.getPair().equals(pair)
                        && realmHistoryData.getOption().equals(option))

                    loadValuesToChart(realmHistoryData.getValues());
                else {
                    clearValuesFromRealm();
                    showRate(true);
                }
            }

        }
    }

    public RateFragmentPresenterImpl() {
        dataRepository = new RestDataRepository();
    }

    @Override
    public void onStop() {
        disposable.dispose();
    }

    @Override
    public String getId() {
        return "rate_fragment";
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

        final String crypto_symbol = getView().getCrypto_selected().symbol;
        final String country_symbol = getView().getCountry_selected().symbol;

        disposable = dataRepository.getRate(crypto_symbol, country_symbol)
                .subscribe(ticker -> {

                    if (!ticker.getError().isEmpty()) {
                        if (getView() != null)
                            getView().showError(R.string.server_error);
                        Log.e(TAG, ticker.getError());

                        return;
                    }

                    double price = ticker.getTicker().getPrice();
                    String text = PriceConverter.convertPrice(price) + " " + ticker.getTicker().getTarget();

                    if (getView() != null) {
                        if (!getView().getTopPanel().getText().equals(text)) {
                            getView().getTopPanel().setText(text);
                            App.getAppInstance().getPreferences().setPrice(text);
                        }

                        ChartOption chartOption = getView().getSelectedChartOption();
                        showChart(crypto_symbol, country_symbol, chartOption, refresh);
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

        final String crypto_symbol = getView().getCrypto_selected().symbol;
        final String crypto_id = getView().getCrypto_selected().id;
        final String country_symbol = getView().getCountry_selected().symbol;

        disposable = dataRepository.getRateCMC(crypto_id, country_symbol)
                .subscribe(array -> {

                    CoinMarketCapTicker ticker = array.get(0);

                    double price = ticker.getPrice(country_symbol);
                    String text = PriceConverter.convertPrice(price) + " " + country_symbol;

                    if (getView() != null) {
                        if (!getView().getTopPanel().getText().equals(text)) {
                            getView().getTopPanel().setText(text);
                            App.getAppInstance().getPreferences().setPrice(text);
                        }

                        ChartOption chartOption = getView().getSelectedChartOption();
                        showChart(crypto_symbol, country_symbol, chartOption, refresh);
                    }

                }, error -> {
                    //if (getView() != null) getView().showError(R.string.connection_error);
                    showRateCryptonator(refresh);
                    setRefreshing(false);
                });
    }

    @Override
    public void showChart(String crypto, String country, ChartOption chartOption, boolean refresh) {
        Log.i(TAG, "showChart()");

        getHistory(crypto, country, chartOption);
    }

    private void getHistory(String from_symbol, String to_symbol, ChartOption chartOption) {
        Log.i(TAG, "getHistory()");

        Consumer<HistoData> onSubscribe = result -> {
            if (getView() == null) return;

            if (result == null || result.response.equals("Error") ||
                    result.getData() == null || result.getData().isEmpty()) {
                getView().getRateChart().getChart().clear();
                setRefreshing(false);
                return;
            }

            if (!valuesEqualWithRealm(result.getData())) {
                saveValuesToRealm(result.getData(), from_symbol + to_symbol, chartOption);
                loadValuesToChart(result.getData());
            } else {
                if (getView().getRateChart().getChart().isEmpty())
                    loadValuesToChart(result.getData());
            }

            setRefreshing(false);
        };

        Consumer<Throwable> onError = error -> {
            if (getView() == null) return;

            getView().getRateChart().getChart().clear();
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

    private void saveValuesToRealm(List<Datum> values, String pair, ChartOption option) {
        Log.i(TAG, "saveValuesToRealm()");

        RealmList<Datum> realmList = new RealmList<>();
        realmList.addAll(values);
        RealmHistoryData realmHistoryData = new RealmHistoryData(realmList, pair, option);

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
