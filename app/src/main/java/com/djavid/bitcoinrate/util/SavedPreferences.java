package com.djavid.bitcoinrate.util;

import android.content.SharedPreferences;


public class SavedPreferences {

    private SharedPreferences sharedPreferences;


    public SavedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }


    public String getToken() {
        return sharedPreferences.getString("token", "");
    }
    public void setToken(String token) {
        sharedPreferences
                .edit()
                .putString("token", token)
                .apply();
    }

    public long getTokenId() {
        return sharedPreferences.getLong("token_id", 0);
    }
    public void setTokenId(long id) {
        sharedPreferences
                .edit()
                .putLong("token_id", id)
                .apply();
    }

    public String getLeftSpinnerValue() {
        return sharedPreferences.getString("left_spinner_value", "BTC");
    }
    public void setLeftSpinnerValue(String value) {
        sharedPreferences
                .edit()
                .putString("left_spinner_value", value)
                .apply();
    }

    public String getRightSpinnerValue() {
        return sharedPreferences.getString("right_spinner_value", "USD");
    }
    public void setRightSpinnerValue(String value) {
        sharedPreferences
                .edit()
                .putString("right_spinner_value", value)
                .apply();
    }

    public String getTitleFormat() {
        return sharedPreferences.getString("title_format", "codes");
    }
    public void setTitleFormat(String value) {
        //value = 'codes', 'titles'
        sharedPreferences
                .edit()
                .putString("title_format", value)
                .apply();
    }

    public String getShowedPriceChange() {
        return sharedPreferences.getString("display_price_change", "day");
    }
    public void setShowedPriceChange(String value) {
        //value = 'hour', 'day', 'week'
        sharedPreferences
                .edit()
                .putString("display_price_change", value)
                .apply();
    }

    public String getSortingDirection() {
        return sharedPreferences.getString("sorting_direction", "ascending");
    }
    public void setSortingDirection(String value) {
        //value = 'ascending', 'descending'
        sharedPreferences
                .edit()
                .putString("sorting_direction", value)
                .apply();
    }

    public String getSortingParameter() {
        return sharedPreferences.getString("sorting_parameter", "title");
    }
    public void setSortingParameter(String value) {
        //value = 'title', 'price', 'market_cap', 'hour', 'day', 'week'
        sharedPreferences
                .edit()
                .putString("sorting_parameter", value)
                .apply();
    }

    public String getPrice() {
        return sharedPreferences.getString("price", "");
    }
    public void setPrice(String value) {
        sharedPreferences
                .edit()
                .putString("price", value)
                .apply();
    }

}
