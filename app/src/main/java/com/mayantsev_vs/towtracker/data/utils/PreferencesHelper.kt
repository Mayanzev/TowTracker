package com.mayantsev_vs.towtracker.data.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var isOrderStarted: Boolean
        get() = sharedPreferences.getBoolean(KEY_ORDER_STARTED, false)
        set(value) {
            sharedPreferences.edit().putBoolean(KEY_ORDER_STARTED, value).apply()
        }

    companion object {
        private const val PREF_NAME = "tow_tracker_preferences"
        private const val KEY_ORDER_STARTED = "key_order_started"
    }
}