package com.mayantsev_vs.towtracker.main.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.mayantsev_vs.towtracker.R
import com.mayantsev_vs.towtracker.sl.ViewModelFactory
import com.mayantsev_vs.towtracker.databinding.ActivityMainBinding
import com.mayantsev_vs.towtracker.main.utils.openFragment
import com.mayantsev_vs.towtracker.main.utils.checkPermission
import com.mayantsev_vs.towtracker.main.utils.showToast
import com.mayantsev_vs.towtracker.login.presentation.login.LoginFragment
import com.mayantsev_vs.towtracker.login.presentation.login.LoginViewModel
import kotlin.getValue

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val requestCodePostNotifications = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val loginViewModel: LoginViewModel by viewModels {
            ViewModelFactory(applicationContext)
        }

        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
                        or WindowInsetsCompat.Type.displayCutout()
            )
            v.updatePadding(
                left = bars.left,
                top = bars.top,
                right = bars.right,
                bottom = bars.bottom,
            )
            WindowInsetsCompat.CONSUMED
        }

        loginViewModel.init()

        loginViewModel.registeredLiveData.observe(this) {
            if (it) {
                openFragment(MainFragment())
            } else {
                openFragment(LoginFragment())
            }
        }

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
}