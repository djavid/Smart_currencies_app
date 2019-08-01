package com.djavid.bitcoinrate.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import com.crashlytics.android.Crashlytics
import com.djavid.bitcoinrate.SHARED_PREFERENCES_NAME
import com.djavid.bitcoinrate.contracts.main.MainActivityModule
import com.djavid.bitcoinrate.di.CommonModule
import com.djavid.bitcoinrate.di.RateFragmentModule
import com.djavid.bitcoinrate.di.TickersFragmentModule
import com.djavid.bitcoinrate.network.ApiInterface
import com.djavid.bitcoinrate.util.SavedPreferences
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration
import net.danlew.android.joda.JodaTimeAndroid
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class App : Application(), KodeinAware, KodeinApp {
	
	private var apiInterface: ApiInterface? = null
	private var sharedPreferences: SharedPreferences? = null
	private var savedPreferences: SavedPreferences? = null
	
	companion object {
		var appInstance: App? = null
			private set
	}
	
	override val kodein by Kodein.lazy {
		import(CommonModule(this@App).kodein)
		
		bind<KodeinApp>() with singleton { this@App }
	}
	
	
	override fun onCreate() {
		super.onCreate()
		
		Fabric.with(this, Crashlytics())
		
		getSharedPreferences()
		//AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
		JodaTimeAndroid.init(this)
		
		Realm.init(this)
		val config = RealmConfiguration.Builder()
				.deleteRealmIfMigrationNeeded()
				.build()
		Realm.setDefaultConfiguration(config)
		
		appInstance = applicationContext as App
		
	}
	
	override fun mainComponent(activity: Activity) = Kodein {
		extend(kodein)
		import(MainActivityModule(this@App).kodein)
	}
	
	override fun rateComponent(fragment: Fragment) = Kodein {
		extend(kodein)
		import(RateFragmentModule().kodein)
	}
	
	override fun tickerComponent(fragment: Fragment) = Kodein {
		extend(kodein)
		import(TickersFragmentModule().kodein)
	}
	
	override fun settingsFragmentComponent(fragment: Fragment) = Kodein {
		extend(kodein)
	}
	
	fun getApiInterface(): ApiInterface? {
		if (apiInterface == null)
			apiInterface = buildApiInterface()
		return apiInterface
	}
	
	private fun getSharedPreferences(): SharedPreferences? {
		if (sharedPreferences == null)
			sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
		
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
	
}
