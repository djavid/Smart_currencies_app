package com.djavid.bitcoinrate.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.djavid.bitcoinrate.Fragments.RateFragment;
import com.djavid.bitcoinrate.Fragments.TicketFragment;
import com.djavid.bitcoinrate.R;


public class MainActivity extends AppCompatActivity implements RateFragment.OnFragmentInteractionListener,
        TicketFragment.OnFragmentInteractionListener {
    final FragmentManager fragmentManager = getFragmentManager();
    Fragment rateFragment, ticketFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            final FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:

                    transaction.replace(R.id.content, rateFragment);
                    transaction.commit();

                    return true;
                case R.id.navigation_cards:

                    transaction.replace(R.id.content, ticketFragment);
                    transaction.commit();

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
        ticketFragment = TicketFragment.newInstance();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
