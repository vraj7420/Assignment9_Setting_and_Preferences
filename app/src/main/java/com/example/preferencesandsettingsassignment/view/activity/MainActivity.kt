package com.example.preferencesandsettingsassignment.view.activity

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.example.preferencesandsettingsassignment.R
import com.example.preferencesandsettingsassignment.adapter.ImageAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var imagesPath: String
    private var listOfAllImages = ArrayList<String>()
    private lateinit var getFileAccessPermission:ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         getFileAccessPermission = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        loadImages()
                    }
                }
            }
        }
    }




    private fun setAdapter() {
        pbImageLoading.visibility=View.VISIBLE
        val imageAdapter = ImageAdapter(this, listOfAllImages)
        gridViewGallery.adapter = imageAdapter
        pbImageLoading.visibility=View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    private fun loadSettingChange() {
        val sharedPrefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val statusBarColor = sharedPrefs.getString("statusBarColor", "#FF6200EE")
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor(statusBarColor)
        val actionBarColor = sharedPrefs.getString("actionBarColor", "#FF6200EE")
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor(actionBarColor)))
        imagesPath = sharedPrefs.getString("photoPath", "DCIM/Camera").toString()
    }

    override fun onResume() {
        super.onResume()
        loadSettingChange()
        storagePermission()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuItemSetting) {
            val intentSetting = Intent(this, SettingActivity::class.java)
            startActivity(intentSetting)
        }
        return true
    }

    private fun storagePermission() {
        if (isPermissionGranted()) {
            loadImages()
        } else {
            tackPermission()
        }
    }

    private fun isPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val readExternalStorage =
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            readExternalStorage == PackageManager.PERMISSION_GRANTED
        }
    }


    private fun loadImages() {
        listOfAllImages.clear()
        val directory =
            File(Environment.getExternalStorageDirectory().toString() + File.separator + imagesPath)
        if (directory.isDirectory) {
            val listFiles = directory.listFiles()
            if (listFiles != null && listFiles.isNotEmpty()) {
                for (i in listFiles.indices) {
                    val filePath = listFiles[i].absolutePath
                    listOfAllImages.add(filePath)
                }
            } else {
                Toast.makeText(
                    this,
                    " is empty. Please load some images in it !",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        setAdapter()
    }

    private fun tackPermission() {

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse(String.format("package%s", applicationContext.packageName))
                getFileAccessPermission.launch(intent)

            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                getFileAccessPermission.launch(intent)
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                101
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadImages()
        }
    }
}
