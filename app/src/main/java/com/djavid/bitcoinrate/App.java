package com.djavid.bitcoinrate;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;

import com.djavid.bitcoinrate.rest.ApiInterface;
import com.djavid.bitcoinrate.util.PresenterProvider;
import com.djavid.bitcoinrate.util.SavedPreferences;

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
    private SavedPreferences savedPreferences;
    private static String SHARED_PREFERENCES_CODE = "bitcoin_rate_app";


    @Override
    public void onCreate() {
        super.onCreate();

//        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
//            e.printStackTrace();
//            FirebaseCrash.report(e);
//        });

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build());

        getPresenterProvider();
        getSharedPreferences();
        getPreferences();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        JodaTimeAndroid.init(this);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        appInstance = (App) getApplicationContext();

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

    private SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null)
            sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_CODE, MODE_PRIVATE);

        return sharedPreferences;
    }

    public SavedPreferences getPreferences() {
        if (savedPreferences == null)
            savedPreferences = new SavedPreferences(getSharedPreferences());

        return savedPreferences;
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

}
