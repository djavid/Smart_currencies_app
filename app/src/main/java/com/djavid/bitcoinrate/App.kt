package com.djavid.bitcoinrate

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatDelegate
import com.djavid.bitcoinrate.rest.ApiInterface
import com.djavid.bitcoinrate.util.PresenterProvider
import com.djavid.bitcoinrate.util.SavedPreferences
import io.realm.Realm
import io.realm.RealmConfiguration
import net.danlew.android.joda.JodaTimeAndroid
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


class App : Application() {
    private var apiInterface: ApiInterface? = null
    private var presenterProvider: PresenterProvider? = null
    private var sharedPreferences: SharedPreferences? = null
    private var savedPreferences: SavedPreferences? = null

    val preferences: SavedPreferences
        get() {
            if (savedPreferences == null)
                savedPreferences = SavedPreferences(getSharedPreferences())

            return savedPreferences
        }


    override fun onCreate() {
        super.onCreate()

        //        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
        //            e.printStackTrace();
        //            FirebaseCrash.report(e);
        //        });

        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build())

        getPresenterProvider()
        getSharedPreferences()
        preferences
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        JodaTimeAndroid.init(this)

        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)

        appInstance = applicationContext as App

    }

    fun getApiInterface(): ApiInterface? {
        if (apiInterface == null)
            apiInterface = buildApiInterface()
        return apiInterface
    }

    fun getPresenterProvider(): PresenterProvider {
        if (presenterProvider == null)
            presenterProvider = PresenterProvider()

        return presenterProvider
    }

    private fun getSharedPreferences(): SharedPreferences? {
        if (sharedPreferences == null)
            sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_CODE, Context.MODE_PRIVATE)

        return sharedPreferences
    }

    private fun buildApiInterface(): ApiInterface {
        val httpClient = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(logging)

        val retrofit = Retrofit.Builder()
                .baseUrl("https://blockchain.info/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build()

        return retrofit.create(ApiInterface::class.java)
    }

    companion object {

        var appInstance: App? = null
            private set
        private val SHARED_PREFERENCES_CODE = "bitcoin_rate_app"

        val context: Context
            get() = appInstance!!.applicationContext
    }

}
