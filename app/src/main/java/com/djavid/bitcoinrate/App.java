package com.djavid.bitcoinrate;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;

import com.djavid.bitcoinrate.model.ApiInterface;
import com.djavid.bitcoinrate.domain.PresenterProvider;
import com.djavid.bitcoinrate.model.DataRepository;
import com.djavid.bitcoinrate.model.RestDataRepository;
import com.djavid.bitcoinrate.util.RxUtils;
import com.google.firebase.iid.FirebaseInstanceId;

import net.danlew.android.joda.JodaTimeAndroid;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class App extends Application {

    private static App appInstance;

    private ApiInterface apiInterface;
    private PresenterProvider presenterProvider;
    private SharedPreferences sharedPreferences;

    private static String SHARED_PREFERENCES_CODE = "bitcoin_rate_app";

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build());

        getPresenterProvider();
        getSharedPreferences();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        JodaTimeAndroid.init(this);
        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);

        appInstance = (App) getApplicationContext();

//        if (!sharedPreferences.contains("token_id") ||
//                !sharedPreferences.contains("token")) {
            String token = FirebaseInstanceId.getInstance().getToken();
        System.out.println(token);
            sendTokenToServer(token);
//        }

    }

    public static Context getContext() {
        return appInstance.getApplicationContext();
    }

    public static App getAppInstance() {
        return appInstance;
    }

    public ApiInterface getApiInterface() {
        if (apiInterface == null)
            apiInterface = buildApiInterface();
        return apiInterface;
    }

    public PresenterProvider getPresenterProvider() {
        if (presenterProvider == null)
            presenterProvider = new PresenterProvider();

        return presenterProvider;
    }

    public SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null)
            sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_CODE, MODE_PRIVATE);

        return sharedPreferences;
    }

    private ApiInterface buildApiInterface() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://blockchain.info/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();

        return retrofit.create(ApiInterface.class);
    }

    private void sendTokenToServer(String token) {

        long id;
        //if not found preference then is default 0
        id = App.getAppInstance().sharedPreferences.getLong("token_id", 0);

        DataRepository dataRepository = new RestDataRepository();
        dataRepository.registerToken(token, id)
                .compose(RxUtils.applySingleSchedulers())
                .subscribe(response -> {

                    if (response.error.isEmpty()) {

                        if (response.id != 0) {
                            App.getAppInstance()
                                    .sharedPreferences
                                    .edit()
                                    .putLong("token_id", response.id)
                                    .apply();

                            App.getAppInstance()
                                    .sharedPreferences
                                    .edit()
                                    .putString("token", token)
                                    .apply();
                        }
                    }
                });
    }

}
