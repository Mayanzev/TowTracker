package com.mayantsev_vs.towtracker.presentation.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.mayantsev_vs.towtracker.R


class AppearanceSettingsFragment : PreferenceFragmentCompat() {
    private lateinit var timePreference: Preference
    private lateinit var colorPreference: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.appearance_settings, rootKey)
        init()
    }

    private fun init() {
        timePreference = findPreference("update_time_key")!!
        colorPreference = findPreference("color_key")!!
        val changeListener = onChangeListener()
        timePreference.onPreferenceChangeListener = changeListener
        colorPreference.onPreferenceChangeListener = changeListener
        initPrefs()
    }

    private fun onChangeListener(): Preference.OnPreferenceChangeListener {
        return Preference.OnPreferenceChangeListener {
            pref, value ->
                when (pref.key) {
                    "update_time_key" -> onTimeChange(value.toString())
                    "color_key" -> pref.icon?.setTint(Color.parseColor(value.toString()))
                }
            true
        }
    }

    private fun onTimeChange(value: String) {
        val nameArray = resources.getStringArray(R.array.loc_time_update_name)
        val valueArray = resources.getStringArray(R.array.loc_time_update_value)
        val title = timePreference.title.toString().substringBefore(":")
        val pos = valueArray.indexOf(value)
        timePreference.title = "$title: ${nameArray[pos]}"
    }

    private fun initPrefs() {
        val pref = timePreference.preferenceManager.sharedPreferences
        val nameArray = resources.getStringArray(R.array.loc_time_update_name)
        val valueArray = resources.getStringArray(R.array.loc_time_update_value)
        val title = timePreference.title
        val pos = valueArray.indexOf(pref?.getString("update_time_key", "1000"))
        timePreference.title = "$title: ${nameArray[pos]}"

        val trackColor = pref?.getString("color_key", "#0077FF")
        colorPreference.icon?.setTint(Color.parseColor(trackColor))
    }
}