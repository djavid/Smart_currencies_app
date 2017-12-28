package com.djavid.bitcoinrate.presenter.implementations;

import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.core.BasePresenter;
import com.djavid.bitcoinrate.core.Router;
import com.djavid.bitcoinrate.model.DataRepository;
import com.djavid.bitcoinrate.model.RestDataRepository;
import com.djavid.bitcoinrate.model.dto.coinmarketcap.CoinMarketCapTicker;
import com.djavid.bitcoinrate.model.dto.heroku.Subscribe;
import com.djavid.bitcoinrate.model.dto.heroku.Ticker;
import com.djavid.bitcoinrate.model.dto.realm.RealmSubscribeList;
import com.djavid.bitcoinrate.model.dto.realm.RealmTickerList;
import com.djavid.bitcoinrate.presenter.interfaces.TickerFragmentPresenter;
import com.djavid.bitcoinrate.util.Codes;
import com.djavid.bitcoinrate.util.DateFormatter;
import com.djavid.bitcoinrate.view.adapter.TickerItem;
import com.djavid.bitcoinrate.view.interfaces.TickerFragmentView;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.realm.Realm;


public class TickerFragmentPresenterImpl extends BasePresenter<TickerFragmentView, Router, Object>
        implements TickerFragmentPresenter {

    private final String TAG = this.getClass().getSimpleName();
    private Disposable disposable = Disposables.empty();
    private DataRepository dataRepository;
    private List<Ticker> tickers;
    private List<Subscribe> subscribes;


    public TickerFragmentPresenterImpl() {
        dataRepository = new RestDataRepository();
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart()");

        if (getView() != null) {

            List<Ticker> tickersFromRealm = getTickersFromRealm();
            List<Subscribe> subscribesFromRealm = getSubscribesFromRealm();

            if (!tickersFromRealm.isEmpty()) {
                tickers = tickersFromRealm;
                subscribes = subscribesFromRealm;

                getView().addAllTickers(tickers, subscribes);
            }
        }
    }

    @Override
    public void onStop() {
        disposable.dispose();
    }

    @Override
    public String getId() {
        return "ticker_fragment";
    }

    @Override
    public void saveInstanceState(Object instanceState) {
        setInstanceState(instanceState);
    }


    @Override
    public void addTickerFromServer(long token_id, long ticker_id) {
        Log.i(TAG, "addTickerFromServer(" + token_id + ", " + ticker_id +")");

        disposable = dataRepository.getTickerByTokenIdAndTickerId(token_id, ticker_id)
                .subscribe(ticker -> {

                    tickers.add(ticker);
                    if (getView() != null) getView().addTickerToAdapter(ticker);

                }, error -> {
                    if (getView() != null)
                        getView().showError(R.string.error_loading_ticker);
                });
    }

    @Override
    public void getAllTickers(boolean refresh) {
        Log.i(TAG, "getAllTickers(" + refresh + ")");
        if (refresh) setRefreshing(true);

        long token_id = App.getAppInstance().getPreferences().getTokenId();

        if (token_id == 0) {
            sendTokenToServer();

        } else {
            disposable = dataRepository.getTickersByTokenId(token_id)
                    .subscribe(tickerList -> {

                        tickers = tickerList;
                        getAllSubscribes();

                    }, error -> {
                        if (getView() != null)
                            getView().showError(R.string.unable_to_load_from_server);
                        setRefreshing(false);
                    });
        }
    }

    private void getAllSubscribes() {
        Log.i(TAG, "getAllSubscribes()");

        long token_id = App.getAppInstance().getPreferences().getTokenId();

        disposable = dataRepository.getSubscribesByTokenId(token_id)
                .subscribe(subscribeList -> {

                    subscribes = subscribeList;

                    List<Ticker> tickerListRealm = getTickersFromRealm();
                    List<Subscribe> subscribeListRealm = getSubscribesFromRealm();

                    if (!tickerListRealm.equals(tickers) || !subscribeListRealm.equals(subscribes)){
                        Log.i(TAG, "tickers and subscribes are NOT EQUAL");
                        saveDataToRealm(tickers, subscribes);

                        if (getView() != null) {
                            getView().addAllTickers(tickers, subscribes);
                        }
                    } else {
                        Log.i(TAG, "tickers and subscribes are EQUAL");
                    }

                    setRefreshing(false);

                }, error -> {
                    setRefreshing(false);
                });
    }



    @Override
    public void loadTickerPrice(TickerItem tickerItem) {
        loadTickerPriceCMC(tickerItem);
    }

    @Override
    public void loadTickerPriceCryptonator(TickerItem tickerItem) {
        setRefreshing(true);

        String curr1 = tickerItem.getTickerItem().getCryptoId();
        String curr2 = tickerItem.getTickerItem().getCountryId();

        disposable = dataRepository.getRate(curr1, curr2)
                .subscribe(ticker -> {
                    if (!ticker.getError().isEmpty()) {
                        if (getView() != null) getView().showError(R.string.unable_to_load_from_server);
                        Log.e(TAG, ticker.getError());
                        return;
                    }

                    String price = DateFormatter.convertPrice(ticker.getTicker().getPrice());
                    tickerItem.setPrice(price);

                    setRefreshing(false);

                }, error -> {
                    setRefreshing(false);
                });
    }

    @Override
    public void loadTickerPriceCMC(TickerItem tickerItem) {
        setRefreshing(true);

        String code_crypto = tickerItem.getTickerItem().getCryptoId();
        final String code_crypto_full = Codes.getCryptoCurrencyId(code_crypto);
        String code_country = tickerItem.getTickerItem().getCountryId();

        disposable = dataRepository.getRateCMC(code_crypto_full, code_country)
                .subscribe(array -> {
                    CoinMarketCapTicker ticker = array.get(0);

                    double price = ticker.getPrice(code_country);
                    String text = DateFormatter.convertPrice(price);

                    tickerItem.setPrice(text);
                    setRefreshing(false);

                }, error -> {
                    setRefreshing(false);
                });
    }



    private void sendTokenToServer() {

        String token = FirebaseInstanceId.getInstance().getToken();
        if (token == null || token.isEmpty()) return;

        long id;
        //if not found preference then is default 0
        id = App.getAppInstance().getPreferences().getTokenId();

        dataRepository.registerToken(token, id)
                .subscribe(response -> {

                    if (response.error.isEmpty()) {

                        //success
                        if (response.id != 0) {
                            saveToPreferences(response.id, token);
                            getAllTickers(false);
                        }

                    } else if (!response.error.isEmpty() && response.id != 0) {

                        //device id was already registered
                        saveToPreferences(response.id, token);
                        getAllTickers(false);

                    } else {
                        //something gone wrong
                        setRefreshing(false);
                        if (getView() != null) getView().showError(R.string.connection_error);
                    }
                });
    }

    private void saveToPreferences(long token_id, String token) {
        App.getAppInstance().getPreferences().setTokenId(token_id);
        App.getAppInstance().getPreferences().setToken(token);
    }

    @Override
    public void deleteTicker(long ticker_id) {

        disposable = dataRepository.deleteTicker(ticker_id)
                .subscribe(() -> {
                    Log.i(TAG, "Successfully deleted ticker with id = " + ticker_id);
                }, error -> {
                    if (getView() != null) getView().showError(R.string.error_deleting_ticker);
                });
    }

    @Override
    public List<Ticker> getTickersLocal() {
        return tickers;
    }


    private List<Ticker> getTickersFromRealm() {
        Log.i(TAG, "getTickersFromRealm()");

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmTickerList list = realm.where(RealmTickerList.class).findFirst();
        realm.commitTransaction();

        List<Ticker> res = new ArrayList<>();
        if (list != null) {
            res.addAll(list.getList());
        }

        return res;
    }

    private List<Subscribe> getSubscribesFromRealm() {
        Log.i(TAG, "getSubscribesFromRealm()");

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmSubscribeList list = realm.where(RealmSubscribeList.class).findFirst();
        realm.commitTransaction();

        List<Subscribe> res = new ArrayList<>();
        if (list != null) {
            res.addAll(list.getList());
        }

        return res;
    }

    private void saveDataToRealm(List<Ticker> tickers, List<Subscribe> subscribes) {
        Log.i(TAG, "saveDataToRealm()");

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        realm.where(RealmTickerList.class).findAll().deleteAllFromRealm();
        realm.where(RealmSubscribeList.class).findAll().deleteAllFromRealm();

        RealmTickerList realmTickerList = new RealmTickerList(tickers);
        RealmSubscribeList realmSubscribeList = new RealmSubscribeList(subscribes);
        realm.copyToRealm(realmTickerList);
        realm.copyToRealm(realmSubscribeList);

        realm.commitTransaction();
    }

    private void setRefreshing(boolean key) {
        if (getView() != null) {
            if (getView().getRefreshLayout() != null)
                getView().getRefreshLayout().setRefreshing(key);
        }
    }

    public List<Ticker> sortTickers(List<Ticker> tickers) {

        String direction = App.getAppInstance().getPreferences().getSortingDirection();
        String parameter = App.getAppInstance().getPreferences().getSortingParameter();

        List<Ticker> sorted_tickers;

        switch (parameter) {

            case "title":
                sorted_tickers = Stream.of(tickers)
                        .sorted((a, b) -> {
                            String a_title = Codes.getCryptoCurrencyId(a.getCryptoId());
                            String b_title = Codes.getCryptoCurrencyId(b.getCryptoId());

                            return a_title.compareTo(b_title);
                        })
                        .collect(Collectors.toList());
                break;

            case "price":
                sorted_tickers = Stream.of(tickers)
                        .sorted((a, b) -> Double.compare(
                                a.getTicker().getPrice(),
                                b.getTicker().getPrice()))
                        .collect(Collectors.toList());
                break;

            case "market_cap":
                sorted_tickers = Stream.of(tickers)
                        .sorted((a, b) -> Double.compare(
                                a.getTicker().getMarket_cap_usd(),
                                b.getTicker().getMarket_cap_usd()))
                        .collect(Collectors.toList());
                break;

            case "hour":
                sorted_tickers = Stream.of(tickers)
                        .sorted((a, b) -> Double.compare(
                                a.getTicker().getPercent_change_1h(),
                                b.getTicker().getPercent_change_1h()))
                        .collect(Collectors.toList());
                break;

            case "day":
                sorted_tickers = Stream.of(tickers)
                        .sorted((a, b) -> Double.compare(
                                a.getTicker().getPercent_change_24h(),
                                b.getTicker().getPercent_change_24h()))
                        .collect(Collectors.toList());
                break;

            case "week":
                sorted_tickers = Stream.of(tickers)
                        .sorted((a, b) -> Double.compare(
                                a.getTicker().getPercent_change_7d(),
                                b.getTicker().getPercent_change_7d()))
                        .collect(Collectors.toList());
                break;

            default:
                sorted_tickers = tickers;
                break;
        }

        if (direction.equals("descending"))
            Collections.reverse(sorted_tickers);

        return sorted_tickers;
    }

    public List<Ticker> getTickers() {
        return tickers;
    }

    public List<Subscribe> getSubscribes() {
        return subscribes;
    }
}
