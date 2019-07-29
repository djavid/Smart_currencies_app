package com.djavid.bitcoinrate.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.djavid.bitcoinrate.App
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.core.Router
import com.djavid.bitcoinrate.util.Config.ALLOWED_AMOUNT
import com.djavid.bitcoinrate.util.Config.GOOGLE_PLAY_LICENSE_KEY
import com.djavid.bitcoinrate.util.Config.PURCHASE_PRODUCT_ID
import com.djavid.bitcoinrate.view.adapter.TickerItem
import com.djavid.bitcoinrate.view.dialog.CreateLabelDialog
import com.djavid.bitcoinrate.view.dialog.PurchaseDialog
import com.djavid.bitcoinrate.view.fragment.RateFragment
import com.djavid.bitcoinrate.view.fragment.SettingsFragment
import com.djavid.bitcoinrate.view.fragment.TickerFragment
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


class MainActivity : AppCompatActivity(), Router, BillingProcessor.IBillingHandler {

    @BindView(R.id.navigation)
    internal var navigation: BottomNavigationViewEx? = null

    internal val TAG = javaClass.simpleName
    internal val TAG_RATE = "TAG_RATE"
    internal val TAG_TICKER = "TAG_TICKER"
    internal val TAG_SETTINGS = "TAG_SETTINGS"
    internal val TAG_CREATE_LABEL_DIALOG = "TAG_CREATE_LABEL_DIALOG"
    internal val TAG_PURCHASE_DIALOG = "TAG_PURCHASE_DIALOG"

    private var fragmentManager: FragmentManager? = null
    private var rateFragment: Fragment? = null
    private var tickerFragment: Fragment? = null
    private var settingsFragment: Fragment? = null
    override var selectedTickerItem: TickerItem? = null
        private set
    private var billingProcessor: BillingProcessor? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {

            R.id.navigation_rate -> {
                changeFragment(rateFragment, TAG_RATE, true)
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_tickers -> {
                changeFragment(tickerFragment, TAG_TICKER, true)
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_settings -> {
                changeFragment(settingsFragment, TAG_SETTINGS, true)
                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }


    private val subscribesAmount: Int
        get() = App.appInstance!!.preferences.subscribesAmount


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)

        rateFragment = RateFragment.newInstance()
        tickerFragment = TickerFragment.newInstance()
        settingsFragment = SettingsFragment.newInstance()
        fragmentManager = supportFragmentManager

        //BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation!!.onNavigationItemSelectedListener = mOnNavigationItemSelectedListener
        navigation!!.selectedItemId = R.id.navigation_rate

        navigation!!.enableAnimation(false)
        navigation!!.enableShiftingMode(false)
        navigation!!.enableItemShiftingMode(false)
        navigation!!.setTextVisibility(false)

        val iconSize = 30
        navigation!!.setIconSize(iconSize.toFloat(), iconSize.toFloat())
        navigation!!.itemHeight = BottomNavigationViewEx.dp2px(this, (iconSize + 16).toFloat())

        billingProcessor = BillingProcessor(this, GOOGLE_PLAY_LICENSE_KEY, this)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onDestroy() {
        if (billingProcessor != null) {
            billingProcessor!!.release()
        }
        super.onDestroy()
    }

    override fun goBack() {}

    override fun onBackPressed() {

        val entryCount = fragmentManager!!.backStackEntryCount

        // if first fragment is not on screen, just pop back to the previous fragment.
        if (entryCount > 1) {

            fragmentManager!!.popBackStack()

            val tag = fragmentManager!!.getBackStackEntryAt(entryCount - 2).name

            when (tag) {
                TAG_RATE -> navigation!!.selectedItemId = R.id.navigation_rate
                TAG_TICKER -> navigation!!.selectedItemId = R.id.navigation_tickers
                TAG_SETTINGS -> navigation!!.selectedItemId = R.id.navigation_settings
            }

            return
        }

        // if first fragment is showing, then finish the activity.
        finish()
    }

    override fun showCreateLabelDialog(tickerItem: TickerItem) {
        Log.i(TAG, "showCreateLabelDialog($tickerItem)")

        selectedTickerItem = tickerItem

        if (subscribesAmount >= ALLOWED_AMOUNT) {

            if (!billingProcessor!!.isSubscribed(PURCHASE_PRODUCT_ID)) {
                val purchaseDialogFragment = PurchaseDialog.newInstance()
                purchaseDialogFragment.show(fragmentManager!!, TAG_PURCHASE_DIALOG)
            } else {
                val dialog = CreateLabelDialog.newInstance()
                dialog.show(fragmentManager!!, TAG_CREATE_LABEL_DIALOG)
            }

        } else {

            val dialog = CreateLabelDialog.newInstance()
            dialog.show(fragmentManager!!, TAG_CREATE_LABEL_DIALOG)

        }

    }

    fun changeFragment(fragment: Fragment?, tag: String, addBackStack: Boolean) {
        Log.i(TAG, "changeFragment")

        try {

            val existFragment = fragmentManager!!.findFragmentByTag(tag)
            val ft = fragmentManager!!.beginTransaction()
            //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN); GOOGLE BUG

            if (existFragment == null) {

                // fragment not in back stack, create it.
                ft.replace(R.id.container, fragment, tag)

                if (addBackStack)
                    ft.addToBackStack(tag)
                ft.commit()

                Log.w(TAG, "$tag added to the backstack")

            } else {

                // fragment in back stack, call it back.
                ft.replace(R.id.container, existFragment, tag)
                if (addBackStack) {
                    fragmentManager!!.popBackStack(tag, 0)
                }
                ft.commit()

                Log.w(TAG, "$tag fragment returned back from backstack")

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (!billingProcessor!!.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        Log.w(TAG, "onProductPurchased()")

        Toast.makeText(applicationContext,
                getString(R.string.successful_purchase), Toast.LENGTH_LONG).show()
    }

    override fun onPurchaseHistoryRestored() {
        Log.w(TAG, "onPurchaseHistoryRestored()")
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        Log.e(TAG, "onBillingError()")

        error?.printStackTrace()
        showError(R.string.payment_error)
    }

    override fun onBillingInitialized() {
        Log.w(TAG, "onBillingInitialized()")
    }

    fun purchase() {

        val isAvailable = BillingProcessor.isIabServiceAvailable(this)

        if (isAvailable) {

            val isSubsUpdateSupported = billingProcessor!!.isSubscriptionUpdateSupported

            if (isSubsUpdateSupported) {
                billingProcessor!!.subscribe(this, PURCHASE_PRODUCT_ID)
            } else {
                showError(R.string.error_service_unavailable)
            }

        } else {
            showError(R.string.error_service_unavailable)
        }

    }

    fun showError(errorId: Int) {
        try {
            runOnUiThread { Toast.makeText(this, getString(errorId), Toast.LENGTH_SHORT).show() }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
