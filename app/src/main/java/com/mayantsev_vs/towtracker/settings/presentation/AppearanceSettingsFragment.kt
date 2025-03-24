package com.mayantsev_vs.towtracker.settings.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.mayantsev_vs.towtracker.R


class AppearanceSettingsFragment : PreferenceFragmentCompat() {
    private lateinit var timePreference: Preference
    private lateinit var colorPreference: Preference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    parentFragmentManager.popBackStack()
                }
            }
        )
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.appearance_settings, rootKey)
        init()
    }

    private fun init() {
        timePreference = findPreference(KEY_UPDATE_TIME)!!
        colorPreference = findPreference(KEY_COLOR)!!
        val changeListener = onChangeListener()
        timePreference.onPreferenceChangeListener = changeListener
        colorPreference.onPreferenceChangeListener = changeListener
        initPrefs()

    }

    private fun onChangeListener(): Preference.OnPreferenceChangeListener {
        return Preference.OnPreferenceChangeListener {
            pref, value ->
                when (pref.key) {
                    KEY_UPDATE_TIME -> onTimeChange(value.toString())
                    KEY_COLOR -> onColorChange(pref, value.toString())
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

    private fun onColorChange(pref: Preference, color: String) {
        pref.icon?.setTint(Color.parseColor(color))
    }

    private fun initPrefs() {
        val pref = timePreference.preferenceManager.sharedPreferences
        val nameArray = resources.getStringArray(R.array.loc_time_update_name)
        val valueArray = resources.getStringArray(R.array.loc_time_update_value)
        val title = timePreference.title
        val pos = valueArray.indexOf(pref?.getString(KEY_UPDATE_TIME, "1000"))
        timePreference.title = "$title: ${nameArray[pos]}"

        val trackColor = pref?.getString(KEY_COLOR, "#0077FF")
        colorPreference.icon?.setTint(Color.parseColor(trackColor))
    }


    companion object {
        const val KEY_UPDATE_TIME = "update_time_key"
        const val KEY_COLOR = "color_key"
    }
}
