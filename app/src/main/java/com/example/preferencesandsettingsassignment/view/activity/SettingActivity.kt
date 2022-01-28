package com.example.preferencesandsettingsassignment.view.activity

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.preferencesandsettingsassignment.R
import com.example.preferencesandsettingsassignment.view.fragment.SettingFragment


class SettingActivity : AppCompatActivity(),SharedPreferences.OnSharedPreferenceChangeListener{
    private lateinit var sharedPrefs: SharedPreferences
  companion object {
        var imagesPath:String=""
  }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val settingFragment= SettingFragment()
        sharedPrefs= PreferenceManager.getDefaultSharedPreferences(this)
        if(savedInstanceState!=null){
            return
        }
        supportFragmentManager.beginTransaction().replace(android.R.id.content,settingFragment).commit()
        loadSettingChange()
        sharedPrefs.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        loadSettingChange()
    }

    private fun loadSettingChange() {
        val statusBarColor=sharedPrefs.getString("statusBarColor","#FF6200EE")
        val window: Window = window
         window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor(statusBarColor)
        val actionBarColor=sharedPrefs.getString("actionBarColor","#FF6200EE")
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor(actionBarColor)))
        imagesPath =sharedPrefs.getString("photoPath","DCIM/Camera").toString()
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
    loadSettingChange()
    }
}