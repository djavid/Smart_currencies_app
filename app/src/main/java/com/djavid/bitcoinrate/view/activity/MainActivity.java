package com.djavid.bitcoinrate.view.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.adapter.TickerItem;
import com.djavid.bitcoinrate.domain.MainRouter;
import com.djavid.bitcoinrate.model.realm.TickerItemRealm;
import com.djavid.bitcoinrate.view.dialog.CreateLabelDialog;
import com.djavid.bitcoinrate.view.dialog.CreateTickerDialog;
import com.djavid.bitcoinrate.view.fragment.RateFragment;
import com.djavid.bitcoinrate.view.fragment.TickerFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity implements RateFragment.OnFragmentInteractionListener,
        TickerFragment.OnTickerInteractionListener, MainRouter {

    private FragmentManager fragmentManager;
    Fragment rateFragment, ticketFragment;

    final String TAG = getClass().getSimpleName();
    final String TAG_RATE = "TAG_RATE";
    final String TAG_TICKER = "TAG_TICKER";
    final String TAG_CREATE_DIALOG = "TAG_CREATE_DIALOG";
    final String TAG_CREATE_LABEL_DIALOG = "TAG_CREATE_LABEL_DIALOG";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    changeFragment(rateFragment, TAG_RATE, true);

                    return true;
                case R.id.navigation_cards:

                    changeFragment(ticketFragment, TAG_TICKER, true);

                    return true;
                case R.id.navigation_settings:
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        rateFragment = RateFragment.newInstance();
        ticketFragment = TickerFragment.newInstance();

        fragmentManager = getSupportFragmentManager();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    public void changeFragment(Fragment fragment, String tag, boolean addBackStack) {

        Fragment existFragment = fragmentManager.findFragmentByTag(tag);

        if (existFragment == null) {
            // fragment not in back stack, create it.
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content, fragment, tag);
            if (addBackStack) ft.addToBackStack(tag);
            ft.commit();
            Log.w(TAG, tag + " added to the backstack");
        } else {
            //if (existFragment.isAdded()) return;
            // fragment in back stack, call it back.
            FragmentTransaction ft = fragmentManager.beginTransaction();

            //fragmentManager.popBackStackImmediate(tag, 0);
            ft.replace(R.id.content, existFragment, tag);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(TickerItemRealm item) {

    }

    @Override
    public void goBack() {

    }

    @Override
    public void showCreateTickerDialog() {
        CreateTickerDialog dialog = CreateTickerDialog.newInstance();
        dialog.show(fragmentManager, TAG_CREATE_DIALOG);
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
