package com.djavid.bitcoinrate.view.activity;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.core.Router;
import com.djavid.bitcoinrate.model.dto.heroku.Ticker;
import com.djavid.bitcoinrate.view.adapter.NavigationViewAdapter;
import com.djavid.bitcoinrate.view.adapter.TickerItem;
import com.djavid.bitcoinrate.view.dialog.CreateLabelDialog;
import com.djavid.bitcoinrate.view.fragment.RateFragment;
import com.djavid.bitcoinrate.view.fragment.TickerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity implements RateFragment.OnFragmentInteractionListener,
        TickerFragment.OnTickerInteractionListener, Router {

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private FragmentManager fragmentManager;
    Fragment rateFragment, tickerFragment;
    NavigationViewAdapter navigationViewAdapter;

    final String TAG = getClass().getSimpleName();
    final String TAG_RATE = "TAG_RATE";
    final String TAG_TICKER = "TAG_TICKER";
    final String TAG_CREATE_LABEL_DIALOG = "TAG_CREATE_LABEL_DIALOG";


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
        fragmentManager = getSupportFragmentManager();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_rate);

//        fragmentManager = getSupportFragmentManager();
//        navigationViewAdapter = new NavigationViewAdapter(fragmentManager,
//                R.id.container, R.id.navigation_home, savedInstanceState);
//        navigationViewAdapter.attachTo(navigation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume Activity");

//        String tag = fragmentManager
//                .getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 2).getName();
//
//        switch (tag) {
//            case TAG_RATE:
//                navigation.setSelectedItemId(R.id.navigation_rate);
//                break;
//            case TAG_TICKER:
//                navigation.setSelectedItemId(R.id.navigation_tickers);
//                break;
//        }
    }

    public void changeFragment(Fragment fragment, String tag, boolean addBackStack) {

        Fragment existFragment = fragmentManager.findFragmentByTag(tag);
        FragmentTransaction ft = fragmentManager.beginTransaction();

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
                //ft.addToBackStack(tag);
            }
            ft.commit();

            Log.w(TAG, tag + " fragment returned back from backstack");

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        //super.onSaveInstanceState(outState);
//        //navigationViewAdapter.onSaveInstanceState(outState);
//    }

    @Override
    public void onFragmentInteraction(Uri uri) { }

    @Override
    public void onFragmentInteraction(Ticker item) { }

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
            }

            return;
        }

        // if first fragment is showing, then finish the activity.
        finish();
    }

    @Override
    public void showCreateLabelDialog(TickerItem tickerItem) {
        selectedTickerItem = tickerItem;
        CreateLabelDialog dialog = CreateLabelDialog.newInstance();
        dialog.show(fragmentManager, TAG_CREATE_LABEL_DIALOG);
    }

    private TickerItem selectedTickerItem;

    @Override
    public TickerItem getSelectedTickerItem() {
        return selectedTickerItem;
    }
}
