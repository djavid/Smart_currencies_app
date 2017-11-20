package com.djavid.bitcoinrate.domain;

import android.content.SharedPreferences;

/**
 * Created by djavid on 05.08.17.
 */


public class PreferencesWrapper {

    public SharedPreferences sharedPreferences;

    public PreferencesWrapper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }
    
}
