package com.mayantsev_vs.towtracker.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.mayantsev_vs.towtracker.R
import com.mayantsev_vs.towtracker.sl.ViewModelFactory
import com.mayantsev_vs.towtracker.databinding.ActivityMainBinding
import com.mayantsev_vs.towtracker.presentation.map.MapFragment
import com.mayantsev_vs.towtracker.presentation.order.MainOrderFragment
import com.mayantsev_vs.towtracker.data.utils.openFragment
import com.mayantsev_vs.towtracker.presentation.settings.MainSettingsFragment
import com.mayantsev_vs.towtracker.data.utils.checkPermission
import com.mayantsev_vs.towtracker.data.utils.showToast
import com.mayantsev_vs.towtracker.presentation.order.NewOrderFragment
import com.mayantsev_vs.towtracker.presentation.order.OrderViewModel
import kotlin.getValue

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val requestCodePostNotifications = 1
    private val orderViewModel: OrderViewModel by viewModels {
        ViewModelFactory(applicationContext)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.selectedItemId = R.id.id_order

        onBottomNavClick()

        orderViewModel.isOrderStarted.observe(this, Observer { isOrderStarted ->
            if (isOrderStarted) {
                openFragment(MainOrderFragment.newInstance())
            } else {
                openFragment(NewOrderFragment.newInstance())
            }
        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!checkPermission(Manifest.permission.POST_NOTIFICATIONS)) {
                requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    requestCodePostNotifications
                )
            }
        }
    }

    // handles notification permission result and shows a toast message.
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodePostNotifications) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast(getString(R.string.notification_permission_granted))
            } else {
                showToast(getString(R.string.notification_permission_denied))
            }
        }
    }

    // function for handling a click
    private fun onBottomNavClick() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.id_order -> {
                    orderViewModel.isOrderStarted.value?.let { isOrderStarted ->
                        if (isOrderStarted) {
                            openFragment(MainOrderFragment.newInstance())
                        } else {
                            openFragment(NewOrderFragment.newInstance())
                        }
                    }
                }

                R.id.id_map -> openFragment(MapFragment.newInstance())
                R.id.id_main_settings -> openFragment(MainSettingsFragment())
            }
            true
        }
    }

    fun changeBottomNavigation(currentScreen: CurrentScreen) {
        binding.bottomNavigation.selectedItemId = when (currentScreen) {
            CurrentScreen.MAP -> R.id.id_map
            CurrentScreen.ORDER -> R.id.id_order
        }
    }
}