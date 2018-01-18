package com.djavid.bitcoinrate.presenter.implementations;

import android.util.Log;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.core.BasePresenter;
import com.djavid.bitcoinrate.core.Router;
import com.djavid.bitcoinrate.model.RestDataRepository;
import com.djavid.bitcoinrate.model.dto.coinmarketcap.CoinMarketCapTicker;
import com.djavid.bitcoinrate.model.dto.cryptowatch.Market;
import com.djavid.bitcoinrate.model.dto.realm.RealmDoubleList;
import com.djavid.bitcoinrate.model.dto.realm.RealmHistoryData;
import com.djavid.bitcoinrate.presenter.instancestate.RateFragmentInstanceState;
import com.djavid.bitcoinrate.presenter.interfaces.RateFragmentPresenter;
import com.djavid.bitcoinrate.util.Codes;
import com.djavid.bitcoinrate.util.DateFormatter;
import com.djavid.bitcoinrate.view.interfaces.RateFragmentView;
import com.github.mikephil.charting.data.CandleEntry;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
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
                    loadValuesToChart(realmValuesToList(realmFound));
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

                        int intervals = getView().getSelectedChartOption().intervals;
                        long start = Codes.getChartStartDate(getView().getSelectedChartOption());

                        showChart(curr1, curr2, intervals, start, refresh);
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

        final String crypto_id = getView().getLeftSpinner().getSelectedItem().toString();
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

                        int intervals = getView().getSelectedChartOption().intervals;
                        long start = Codes.getChartStartDate(getView().getSelectedChartOption());

                        showChart(crypto_id, country_id, intervals, start, refresh);
                    }

                }, error -> {
                    //if (getView() != null) getView().showError(R.string.connection_error);
                    showRateCryptonator(refresh);
                    setRefreshing(false);
                });
    }

    @Override
    public void showChart(String crypto, String country, int periods, long after, boolean refresh) {
        Log.i(TAG, "showChart()");

        String pair = Codes.getCryptoCurrencySymbol(crypto) + country;

        disposable = dataRepository.getCryptowatchMarkets(pair)
                .subscribe(result -> {

                    List<Market> markets = result.getResult().getMarkets();

                    if (markets != null && markets.size() != 0) {
                        getHistory(markets.get(0).getExchange(), pair, periods, after, refresh);
                    } else {
                        if (getView() != null) getView().getRateChart().getChart().clear();
                        setRefreshing(false);
                    }

                }, error -> {
                    if (getView() != null) {
                        getView().getRateChart().getChart().clear();
                        //getView().showError(R.string.connection_error);
                    }

                    setRefreshing(false);
                });

    }

    private void getHistory(String market, String pair, int periods, long after, boolean refresh) {
        Log.i(TAG, "getHistory()");

        disposable = dataRepository.getHistory(market, pair, periods, after)
                .subscribe(result -> {

                    if (result == null || result.getResult() == null ||
                            result.getResult().getValues() == null) {
                        setRefreshing(false);
                        return;
                    }

                    List<List<Double>> values = result.getResult().getValues();
                    if (values.size() == 0) {
                        getHistory(market, pair, periods, after, refresh);
                        return;
                    }

                    if (!valuesEqualWithRealm(values)) {
                        saveValuesToRealm(values, pair);
                        loadValuesToChart(values);
                    }

                    setRefreshing(false);

                }, error -> {
                    if (getView() != null) {
                        getView().getRateChart().getChart().clear();
                        //getView().showError(R.string.server_error);
                    }
                    setRefreshing(false);
                });
    }


    private boolean valuesEqualWithRealm(List<List<Double>> values) {
        Log.i(TAG, "valuesEqualWithRealm()");

        final RealmHistoryData realmFound = getValuesFromRealm();

        if (realmFound != null) {
            List<List<Double>> savedValues = realmValuesToList(realmFound);

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

    private List<List<Double>> realmValuesToList(RealmHistoryData realmHistoryData) {
        Log.i(TAG, "realmValuesToList()");

        RealmList<RealmDoubleList> realmList = realmHistoryData.getValues();

        List<List<Double>> values = new ArrayList<>();
        for (int i = 0; i < realmList.size(); i++) {
            List<Double> list_to_add = new ArrayList<>();
            list_to_add.addAll(realmList.get(i).getList());
            values.add(list_to_add);
        }

        return values;
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

    private void saveValuesToRealm(List<List<Double>> values, String pair) {
        Log.i(TAG, "saveValuesToRealm()");

        RealmList<RealmDoubleList> realmList = new RealmList<>();
        for (List<Double> doubleList : values) {
            Double[] doubles = doubleList.toArray(new Double[doubleList.size()]);
            RealmDoubleList realmDoubleList = new RealmDoubleList(doubles);
            realmList.add(realmDoubleList);
        }
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

    private void loadValuesToChart(List<List<Double>> values) {
        Log.i(TAG, "loadValuesToChart()");

        List<CandleEntry> entries = new ArrayList<>();
        List<Long> dates = new ArrayList<>();

        System.out.println(values.size() + " --------------------------------------------------");

        for (int i = 0; i < values.size(); i++) {
            long X = values.get(i).get(0).longValue();
            double y = (values.get(i).get(1) + values.get(i).get(2) +
                    values.get(i).get(3) + values.get(i).get(4)) / 4;

            float open = values.get(i).get(1).floatValue();
            float close = values.get(i).get(4).floatValue();

            float high = values.get(i).get(2).floatValue();
            float low = values.get(i).get(3).floatValue();

            //entries.add(new Entry(X, (float) y));
            dates.add(X);
            entries.add(new CandleEntry(i, high, low, open, close));
        }

        if (getView() != null)
            getView().getRateChart().setData(entries, dates);
    }

    private void setRefreshing(boolean key) {
//        if (getView() != null)
//            if (getView().getRefreshLayout() != null)
//                getView().getRefreshLayout().setRefreshing(key);
    }

}
