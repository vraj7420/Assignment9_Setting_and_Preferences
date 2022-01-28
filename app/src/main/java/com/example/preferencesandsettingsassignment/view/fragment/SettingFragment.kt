package com.example.preferencesandsettingsassignment.view.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.preferencesandsettingsassignment.R

class SettingFragment: PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.setting_preference)
    }
}