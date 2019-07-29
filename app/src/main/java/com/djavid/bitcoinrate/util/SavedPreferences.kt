package com.djavid.bitcoinrate.util

import android.content.SharedPreferences


class SavedPreferences(private val sharedPreferences: SharedPreferences) {


    var token: String
        get() = sharedPreferences.getString("token", "")
        set(token) = sharedPreferences
                .edit()
                .putString("token", token)
                .apply()

    var tokenId: Long
        get() = sharedPreferences.getLong("token_id", 0)
        set(id) = sharedPreferences
                .edit()
                .putLong("token_id", id)
                .apply()

    var leftSpinnerValue: String
        get() = sharedPreferences.getString("left_spinner_value", "BTC")
        set(value) = sharedPreferences
                .edit()
                .putString("left_spinner_value", value)
                .apply()

    var rightSpinnerValue: String
        get() = sharedPreferences.getString("right_spinner_value", "USD")
        set(value) = sharedPreferences
                .edit()
                .putString("right_spinner_value", value)
                .apply()

    //value = 'codes', 'titles'
    var titleFormat: String
        get() = sharedPreferences.getString("title_format", "codes")
        set(value) = sharedPreferences
                .edit()
                .putString("title_format", value)
                .apply()

    //value = 'hour', 'day', 'week'
    var showedPriceChange: String
        get() = sharedPreferences.getString("display_price_change", "day")
        set(value) = sharedPreferences
                .edit()
                .putString("display_price_change", value)
                .apply()

    //value = 'ascending', 'descending'
    var sortingDirection: String
        get() = sharedPreferences.getString("sorting_direction", "ascending")
        set(value) = sharedPreferences
                .edit()
                .putString("sorting_direction", value)
                .apply()

    //value = 'title', 'price', 'market_cap', 'hour', 'day', 'week'
    var sortingParameter: String
        get() = sharedPreferences.getString("sorting_parameter", "title")
        set(value) = sharedPreferences
                .edit()
                .putString("sorting_parameter", value)
                .apply()

    var price: String
        get() = sharedPreferences.getString("price", "")
        set(value) = sharedPreferences
                .edit()
                .putString("price", value)
                .apply()

    var notificationSound: Boolean?
        get() = sharedPreferences.getBoolean("notification_sound_enabled", true)
        set(value) = sharedPreferences
                .edit()
                .putBoolean("notification_sound_enabled", value!!)
                .apply()

    var notificationVibration: Boolean?
        get() = sharedPreferences.getBoolean("notification_vibration_enabled", true)
        set(value) = sharedPreferences
                .edit()
                .putBoolean("notification_vibration_enabled", value!!)
                .apply()

    var subscribesAmount: Int
        get() = sharedPreferences.getInt("subscribes_amount", 0)
        set(value) = sharedPreferences
                .edit()
                .putInt("subscribes_amount", value)
                .apply()

}
