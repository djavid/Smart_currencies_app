package com.djavid.bitcoinrate.adapter;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.view.fragment.RateFragment;
import com.djavid.bitcoinrate.view.fragment.TickerFragment;
import com.patloew.navigationviewfragmentadapters.NavigationViewFragmentAdapter;


public class NavigationViewAdapter extends NavigationViewFragmentAdapter {


    public NavigationViewAdapter(FragmentManager fragmentManager, @IdRes int containerId,
                                 @IdRes int defaultMenuItemId, Bundle savedInstanceState) {

        super(fragmentManager, containerId, defaultMenuItemId, savedInstanceState);
    }

    @NonNull
    @Override
    public Fragment getFragment(int menuItemId) {

        switch (menuItemId) {

            case R.id.navigation_rate:
                return RateFragment.newInstance();

            case R.id.navigation_tickers:
                return TickerFragment.newInstance();

            //case R.id.navigation_settings:
            default:
                return RateFragment.newInstance();
        }
    }

}
