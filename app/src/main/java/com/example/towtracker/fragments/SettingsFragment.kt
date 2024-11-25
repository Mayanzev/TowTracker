package com.example.towtracker.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.towtracker.R


class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preferences, rootKey)
    }

}