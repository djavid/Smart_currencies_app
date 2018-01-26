package com.djavid.bitcoinrate.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.core.Router;
import com.djavid.bitcoinrate.util.Codes;
import com.djavid.bitcoinrate.view.adapter.TickerItem;
import com.djavid.bitcoinrate.view.dialog.CreateLabelDialog;
import com.djavid.bitcoinrate.view.dialog.PurchaseDialogFragment;
import com.djavid.bitcoinrate.view.fragment.RateFragment;
import com.djavid.bitcoinrate.view.fragment.SettingsFragment;
import com.djavid.bitcoinrate.view.fragment.TickerFragment;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity implements Router, BillingProcessor.IBillingHandler {

    @BindView(R.id.navigation)
    BottomNavigationViewEx navigation;

    final String TAG = getClass().getSimpleName();
    final String TAG_RATE = "TAG_RATE";
    final String TAG_TICKER = "TAG_TICKER";
    final String TAG_SETTINGS = "TAG_SETTINGS";
    final String TAG_CREATE_LABEL_DIALOG = "TAG_CREATE_LABEL_DIALOG";
    final String TAG_PURCHASE_DIALOG = "TAG_PURCHASE_DIALOG";

    private FragmentManager fragmentManager;
    private Fragment rateFragment, tickerFragment, settingsFragment;
    private TickerItem selectedTickerItem;
    private BillingProcessor billingProcessor;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {

                case R.id.navigation_rate:
                    changeFragment(rateFragment, TAG_RATE, true);
                    return true;

                case R.id.navigation_tickers:
                    changeFragment(tickerFragment, TAG_TICKER, true);
                    return true;

                case R.id.navigation_settings:
                    changeFragment(settingsFragment, TAG_SETTINGS, true);
                    return true;
            }

            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        rateFragment = RateFragment.newInstance();
        tickerFragment = TickerFragment.newInstance();
        settingsFragment = SettingsFragment.newInstance();
        fragmentManager = getSupportFragmentManager();

        //BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_rate);

        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setTextVisibility(false);

        int iconSize = 30;
        navigation.setIconSize(iconSize, iconSize);
        navigation.setItemHeight(BottomNavigationViewEx.dp2px(this, iconSize + 16));

        billingProcessor = new BillingProcessor(this, Codes.GOOGLE_PLAY_LICENSE_KEY, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onDestroy() {
        if (billingProcessor != null) {
            billingProcessor.release();
        }
        super.onDestroy();
    }

    @Override
    public void goBack() {
    }

    @Override
    public void onBackPressed() {

        int entryCount = fragmentManager.getBackStackEntryCount();

        // if first fragment is not on screen, just pop back to the previous fragment.
        if (entryCount > 1) {

            fragmentManager.popBackStack();

            String tag = fragmentManager.getBackStackEntryAt(entryCount - 2).getName();

            switch (tag) {
                case TAG_RATE:
                    navigation.setSelectedItemId(R.id.navigation_rate);
                    break;
                case TAG_TICKER:
                    navigation.setSelectedItemId(R.id.navigation_tickers);
                    break;
                case TAG_SETTINGS:
                    navigation.setSelectedItemId(R.id.navigation_settings);
                    break;
            }

            return;
        }

        // if first fragment is showing, then finish the activity.
        finish();
    }

    @Override
    public void showCreateLabelDialog(TickerItem tickerItem) {
        Log.i(TAG, "showCreateLabelDialog(" + tickerItem + ")");

        selectedTickerItem = tickerItem;

        System.out.println("Subscribes amount = " + getSubscribesAmount());


        if (getSubscribesAmount() >= Codes.ALLOWED_AMOUNT) {

            if (!billingProcessor.isSubscribed(Codes.PURCHASE_PRODUCT_ID)) {
                PurchaseDialogFragment purchaseDialogFragment = PurchaseDialogFragment.newInstance();
                purchaseDialogFragment.show(fragmentManager, TAG_PURCHASE_DIALOG);
            } else {
                CreateLabelDialog dialog = CreateLabelDialog.newInstance();
                dialog.show(fragmentManager, TAG_CREATE_LABEL_DIALOG);
            }

        } else {

            CreateLabelDialog dialog = CreateLabelDialog.newInstance();
            dialog.show(fragmentManager, TAG_CREATE_LABEL_DIALOG);

        }

    }


    private int getSubscribesAmount() {
        return App.getAppInstance().getPreferences().getSubscribesAmount();
    }

    public void changeFragment(Fragment fragment, String tag, boolean addBackStack) {
        Log.i(TAG, "changeFragment");

        try {

            Fragment existFragment = fragmentManager.findFragmentByTag(tag);
            FragmentTransaction ft = fragmentManager.beginTransaction();
            //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN); GOOGLE BUG

            if (existFragment == null) {

                // fragment not in back stack, create it.
                ft.replace(R.id.container, fragment, tag);

                if (addBackStack)
                    ft.addToBackStack(tag);
                ft.commit();

                Log.w(TAG, tag + " added to the backstack");

            } else {

                // fragment in back stack, call it back.
                ft.replace(R.id.container, existFragment, tag);
                if (addBackStack) {
                    fragmentManager.popBackStack(tag, 0);
                }
                ft.commit();

                Log.w(TAG, tag + " fragment returned back from backstack");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public TickerItem getSelectedTickerItem() {
        return selectedTickerItem;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Log.w(TAG, "onProductPurchased()");

        Toast.makeText(getApplicationContext(),
                getString(R.string.successful_purchase), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPurchaseHistoryRestored() {
        Log.w(TAG, "onPurchaseHistoryRestored()");
    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        Log.e(TAG, "onBillingError()");

        if (error != null) error.printStackTrace();
        showError(R.string.payment_error);
    }

    @Override
    public void onBillingInitialized() {
        Log.w(TAG, "onBillingInitialized()");
    }

    public void purchase() {

        boolean isAvailable = BillingProcessor.isIabServiceAvailable(this);

        if (isAvailable) {

            boolean isSubsUpdateSupported = billingProcessor.isSubscriptionUpdateSupported();

            if (isSubsUpdateSupported) {
                billingProcessor.subscribe(this, Codes.PURCHASE_PRODUCT_ID);
            } else {
                showError(R.string.error_service_unavailable);
            }

        } else {
            showError(R.string.error_service_unavailable);
        }

    }

    public void showError(int errorId) {
        Toast.makeText(this, getString(errorId), Toast.LENGTH_SHORT).show();
    }

}
