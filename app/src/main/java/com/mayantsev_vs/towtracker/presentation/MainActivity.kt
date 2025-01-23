package com.mayantsev_vs.towtracker.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.mayantsev_vs.towtracker.R
import com.mayantsev_vs.towtracker.databinding.ActivityMainBinding
import com.mayantsev_vs.towtracker.presentation.fragments.MapFragment
import com.mayantsev_vs.towtracker.presentation.fragments.MainOrderFragment
import com.mayantsev_vs.towtracker.data.utils.openFragment
import com.mayantsev_vs.towtracker.presentation.fragments.MainSettingsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val requestCodePostNotifications  = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.selectedItemId = R.id.id_order

        onBottomNavClick()
        openFragment(MainOrderFragment.newInstance())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    requestCodePostNotifications
                )
            }
        }
    }

    // function for handling a click
    private fun onBottomNavClick() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.id_map -> openFragment(MapFragment.newInstance())
                R.id.id_order -> openFragment(MainOrderFragment.newInstance())
                R.id.id_main_settings -> openFragment(MainSettingsFragment())
            }
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodePostNotifications) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}