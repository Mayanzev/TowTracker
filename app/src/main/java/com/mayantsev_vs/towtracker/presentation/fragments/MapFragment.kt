package com.mayantsev_vs.towtracker.presentation.fragments

import android.Manifest
import android.content.BroadcastReceiver
import com.mayantsev_vs.towtracker.R
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import com.mayantsev_vs.towtracker.presentation.MainApp
import com.mayantsev_vs.towtracker.presentation.MainViewModel
import com.mayantsev_vs.towtracker.databinding.FragmentMapBinding
import com.mayantsev_vs.towtracker.data.db.TrackItem
import com.mayantsev_vs.towtracker.data.location.LocationModel
import com.mayantsev_vs.towtracker.data.location.LocationService
import com.mayantsev_vs.towtracker.data.utils.DialogManager
import com.mayantsev_vs.towtracker.data.utils.DialogManager.PriceListener
import com.mayantsev_vs.towtracker.data.utils.DialogManager.SimpleListener
import com.mayantsev_vs.towtracker.data.utils.DialogManager.showPriceDialog
import com.mayantsev_vs.towtracker.data.utils.TimeUtils
import com.mayantsev_vs.towtracker.data.utils.checkPermission
import com.mayantsev_vs.towtracker.data.utils.showToast
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class MapFragment : Fragment() {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var binding: FragmentMapBinding
    private var timer: Timer? = null
    private var startTime = 0L
    private var pl: Polyline? = null
    private var firstStart = true
    private var locationModel: LocationModel? = null
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private val model: MainViewModel by activityViewModels {
        MainViewModel.ViewModelFactory((requireContext().applicationContext as MainApp).database)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsOsm()
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerPermissions()
        setOnClicks()
        checkServiceState()
        updateTime()
        registerLocationReceiver()
        locationUpdates()
        loadCurrentPrice()
    }

    override fun onResume() {
        super.onResume()
        checkLocationPermission()
        firstStart = true
    }

    // sets up OSM configuration by loading preferences and defining a custom User-Agent.
    private fun settingsOsm() {
        Configuration.getInstance().load(
            activity as ComponentActivity,
            activity?.getSharedPreferences("osm_pref", Context.MODE_PRIVATE)
        )
        val userAgent = "com.mayantsev_vs.towtracker/1.0"
        Configuration.getInstance().userAgentValue = userAgent
    }

    private fun initOSM() = with(binding) {
        pl = Polyline()
        pl?.outlinePaint?.color = Color.parseColor(
            PreferenceManager.getDefaultSharedPreferences(requireContext())
                .getString(KEY_COLOR, "#0077FF")
        )
        map.controller.setZoom(15.0)

        map.setMultiTouchControls(true)

        val myLocationProvider = GpsMyLocationProvider(activity)
        myLocationOverlay = MyLocationNewOverlay(myLocationProvider, map)
        myLocationOverlay.enableMyLocation()
        myLocationOverlay.enableFollowLocation()
        myLocationOverlay.runOnFirstFix {
            map.overlays.clear()
            map.overlays.add(pl)
            map.overlays.add(myLocationOverlay)
        }
    }

    // registers a permission launcher to handle location permissions when requested.
    private fun registerPermissions() {
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                initOSM()
                checkLocationEnabled()
            } else {
                showToast(getString(R.string.location_permission_denied))
            }
        }
    }

    // determines the appropriate permission check logic based on the Android version.
    private fun checkLocationPermission() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> checkPermissionAfter11()
            Build.VERSION.SDK_INT == Build.VERSION_CODES.Q -> checkPermissionAfter10()
            else -> checkPermission10()
        }
    }

    // checks location permissions for Android 11 (API 30) and above, including background location permission.
    @RequiresApi(Build.VERSION_CODES.R)
    private fun checkPermissionAfter11() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            initOSM()
            checkLocationEnabled()
            checkBackgroundPermission()
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // checks location permissions for Android 10 (API 29), including background location permission.
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkPermissionAfter10() {
        if ((checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
                    checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) &&
            checkPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        ) {
            initOSM()
            checkLocationEnabled()
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            )
        }
    }

    // checks location permissions for Android versions below 10 (API 29).
    private fun checkPermission10() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            initOSM()
            checkLocationEnabled()
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // function that is triggered after all permissions have been granted, which asks for permission to use the location
    private fun checkLocationEnabled() {
        val lManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isEnabled = lManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isEnabled) {
            DialogManager.showLocEnableDialog(
                activity as AppCompatActivity,
                object : SimpleListener {
                    override fun onClick() {
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                }
            )
        } else {
            showToast(getString(R.string.location_permission_granted))
        }
    }

    // creates and returns a click listener that handles multiple button clicks based on their IDs
    private fun onClicks(): View.OnClickListener {
        return View.OnClickListener {
            when (it.id) {
                R.id.ivStartStop -> startStopService()
                R.id.ivCenter -> centerLocation()
                R.id.btnSetPrice -> setPrice()
            }
        }
    }

    // binds the created click listener to the corresponding views in the layout using their IDs
    private fun setOnClicks() = with(binding) {
        val listener = onClicks()
        ivStartStop.setOnClickListener(listener)
        ivCenter.setOnClickListener(listener)
        btnSetPrice.setOnClickListener(listener)
    }

    // starts or stops the location tracking service and handles related UI updates.
    private fun startStopService() {
        if (!LocationService.isRunning) {
            startLocationService()
        } else {
            activity?.stopService(Intent(activity, LocationService::class.java))
            binding.ivStartStop.setImageResource(R.drawable.ic_play)
            timer?.cancel()
            val track = getTrackItem()
            DialogManager.showSaveDialog(requireContext(),
                track,
                object : SimpleListener {
                    override fun onClick() {
                        showToast(getString(R.string.track_saved))
                        model.insertTrack(track)
                    }
                })
        }
    }

    // starts the location tracking service in foreground mode and initializes the timer
    private fun startLocationService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity?.startForegroundService(Intent(activity, LocationService::class.java))
        } else {
            activity?.startService(Intent(activity, LocationService::class.java))
        }
        binding.ivStartStop.setImageResource(R.drawable.ic_stop)
        LocationService.startTime = System.currentTimeMillis()
        startTimer()
    }

    // updates the UI and starts the timer if the location service is running
    private fun checkServiceState() {
        if (LocationService.isRunning) {
            binding.ivStartStop.setImageResource(R.drawable.ic_stop)
            startTimer()
        }
    }

    // starts a timer to update the elapsed time in the UI every second
    private fun startTimer() {
        timer?.cancel()
        timer = Timer()
        startTime = LocationService.startTime
        timer?.schedule(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    model.timeData.value = getCurrentTime()
                }
            }
        }, 1000, 1000)
    }

    // observes time data and updates the UI with the current elapsed time
    private fun updateTime() {
        model.timeData.observe(viewLifecycleOwner) {
            binding.tvTime.text = it
        }
    }

    // retrieves the current elapsed time as a formatted string
    private fun getCurrentTime(): String {
        return "${getString(R.string.time)} ${TimeUtils.getTime(System.currentTimeMillis() - startTime)}"
    }

    // function for setting the price per route
    private fun setPrice() {
        showPriceDialog(requireContext(), object : PriceListener {
            override fun onClick(price: String) {
                val finalPrice = price.toBigDecimalOrNull() ?: BigDecimal("0.0")
                binding.btnSetPrice.text = getString(R.string.price_format, finalPrice)
                LocationService.startPrice = finalPrice
                showToast(getString(R.string.price_change_message, "$finalPrice"))
            }
        })
    }

    // function for loading the price per route
    private fun loadCurrentPrice() {
        val currentPrice = LocationService.startPrice
        binding.btnSetPrice.text = getString(R.string.price_format, currentPrice)
    }

    // broadcastReceiver listens for location updates broadcasted by LocationService
    // when receiving an intent with the location data, it extracts the LocationModel and updates the model with the new location
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == LocationService.LOC_MODEL_INTENT) {
                val locationModel = if (Build.VERSION.SDK_INT < 33) {
                    @Suppress("DEPRECATION")
                    intent.getSerializableExtra(LocationService.LOC_MODEL_INTENT) as LocationModel
                } else {
                    intent.getSerializableExtra(
                        LocationService.LOC_MODEL_INTENT,
                        LocationModel::class.java
                    )
                }
                model.locationUpdates.value = locationModel
            }
        }
    }

    // Registers a receiver for location updates by creating an IntentFilter with the specified action
    private fun registerLocationReceiver() {
        val locationFilter = IntentFilter(LocationService.LOC_MODEL_INTENT)
        LocalBroadcastManager.getInstance(activity as AppCompatActivity)
            .registerReceiver(receiver, locationFilter)
    }

    // observes location updates and refreshes UI elements like distance, speed, polyline
    private fun locationUpdates() = with(binding) {
        model.locationUpdates.observe(viewLifecycleOwner) {
            val distance = "${getString(R.string.distance)} ${String.format(Locale.US, "%.1f", it.distance / 1000)} ${getString(R.string.km)}"
            val speed = "${getString(R.string.velocity)} ${String.format(Locale.US, "%.1f", 3.6f * it.velocity)} ${getString(R.string.km_h)}"
            val averageSpeed = "${getString(R.string.average_velocity)} ${getAverageSpeed(it.distance)} ${getString(R.string.km_h)}"
            val price = "${getString(R.string.price)} ${getPrice(it.distance, LocationService.startPrice)} ${getString(R.string.currency_symbol)}"

            tvDistance.text = distance
            tvSpeed.text = speed
            tvAverageSpeed.text = averageSpeed
            tvPrice.text = price
            locationModel = it
            updatePolyline(it.geoPointsList)
        }
    }

    // function for calculating the average speed
    private fun getAverageSpeed(distance: Float): String {
        return String.format(Locale.US, "%.1f", 3.6f * (distance / ((System.currentTimeMillis() - startTime) / 1000.0f))
        )
    }

    // function for price calculation
    private fun getPrice(distance: Float, startPrice: BigDecimal): String {
        val distanceInKm = BigDecimal(distance.toDouble()).divide(BigDecimal(1000), 1, RoundingMode.HALF_UP)
        val price = distanceInKm.multiply(startPrice)
        return String.format(Locale.US, "%.1f", price)
    }

    private fun  centerLocation() {
        binding.map.controller.animateTo(myLocationOverlay.myLocation)
        myLocationOverlay.enableFollowLocation()
    }

    private fun geoPointsToString(list: List<GeoPoint>): String {
        val sb = StringBuilder()
        list.forEach {
            sb.append("${it.latitude}, ${it.longitude}/")
        }
        return sb.toString()
    }

    private fun getTrackItem(): TrackItem {
        return TrackItem(
            null,
            getCurrentTime(),
            TimeUtils.getDate(),
            String.format(Locale.US, "%.1f", locationModel?.distance?.div(1000) ?: 0),
            getAverageSpeed(locationModel?.distance ?: 0.0f),
            geoPointsToString(locationModel?.geoPointsList ?: listOf())
        )
    }

    private fun checkBackgroundPermission() {
        if (!checkPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            DialogManager.showBackgroundPermissionDialog(
                activity as AppCompatActivity,
                object : SimpleListener {
                    override fun onClick() {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", requireActivity().packageName, null)
                        }
                        startActivity(intent)
                    }
                }
            )
        }
    }

    private fun addPoint(list: List<GeoPoint>) {
        if (list.isNotEmpty()) pl?.addPoint(list[list.size - 1])
    }

    private fun fillPolyline(list: List<GeoPoint>) {
        list.forEach {
            pl?.addPoint(it)
        }
    }

    private fun updatePolyline(list: List<GeoPoint>) {
        if (list.size > 1 && firstStart) {
            fillPolyline(list)
            firstStart = false
        } else {
            addPoint(list)
        }
    }

    override fun onDetach() {
        super.onDetach()
        LocalBroadcastManager.getInstance(activity as AppCompatActivity)
            .unregisterReceiver(receiver)
    }


    companion object {
        @JvmStatic
        fun newInstance() = MapFragment()
        const val KEY_COLOR = "color_key"
    }
}
