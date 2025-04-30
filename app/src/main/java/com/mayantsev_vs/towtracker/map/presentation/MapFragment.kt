package com.mayantsev_vs.towtracker.map.presentation

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import com.mayantsev_vs.towtracker.R
import com.mayantsev_vs.towtracker.databinding.FragmentMapBinding
import com.mayantsev_vs.towtracker.main.presentation.MainFragment.CurrentScreen
import com.mayantsev_vs.towtracker.main.presentation.MainViewModel
import com.mayantsev_vs.towtracker.main.utils.DialogManager
import com.mayantsev_vs.towtracker.main.utils.DialogManager.PriceListener
import com.mayantsev_vs.towtracker.main.utils.DialogManager.SimpleListener
import com.mayantsev_vs.towtracker.main.utils.DialogManager.showPriceDialog
import com.mayantsev_vs.towtracker.main.utils.TimeUtils
import com.mayantsev_vs.towtracker.main.utils.checkPermission
import com.mayantsev_vs.towtracker.main.utils.showToast
import com.mayantsev_vs.towtracker.map.data.location.LocationModel
import com.mayantsev_vs.towtracker.map.data.location.LocationService
import com.mayantsev_vs.towtracker.order.presentation.OrderViewModel
import com.mayantsev_vs.towtracker.sl.ViewModelFactory
import com.mayantsev_vs.towtracker.track.data.cache.TrackItem
import com.mayantsev_vs.towtracker.track.presentation.TrackViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.math.BigDecimal
import java.util.Locale


class MapFragment : Fragment() {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private var polyLine: Polyline? = null
    private var firstStart = true
    private var locationModel: LocationModel? = null
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private val mapViewModel: MapViewModel by activityViewModels {
        ViewModelFactory(requireContext().applicationContext)
    }
    private val tracksViewModel: TrackViewModel by activityViewModels {
        ViewModelFactory(requireContext().applicationContext)
    }
    private val orderViewModel: OrderViewModel by activityViewModels {
        ViewModelFactory(requireContext().applicationContext)
    }
    private val mainViewModel: MainViewModel by activityViewModels {
        ViewModelFactory(requireContext().applicationContext)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsOsm()
        _binding = FragmentMapBinding.inflate(inflater, container, false)
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
        observeOrderFinish()
        observeOrderState()

        mapViewModel.progressLiveData.observe(viewLifecycleOwner) {
            binding.mapProgress.visibility = it
        }
    }

    override fun onResume() {
        super.onResume()
        checkLocationPermission()
        firstStart = true
    }

    override fun onDetach() {
        super.onDetach()
        LocalBroadcastManager.getInstance(activity as AppCompatActivity)
            .unregisterReceiver(receiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun settingsOsm() {
        Configuration.getInstance().load(
            activity as ComponentActivity,
            activity?.getSharedPreferences("osm_pref", Context.MODE_PRIVATE)
        )
        val userAgent = "com.mayantsev_vs.towtracker/1.0"
        Configuration.getInstance().userAgentValue = userAgent
    }

    private fun initOSM() = with(binding) {
        polyLine = Polyline()
        polyLine?.outlinePaint?.color = Color.parseColor(
            PreferenceManager.getDefaultSharedPreferences(requireContext())
                .getString(KEY_COLOR, "#0077FF")
        )
        map.controller.setZoom(15.0)
        map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        map.setMultiTouchControls(true)
        val myLocationProvider = GpsMyLocationProvider(activity)
        myLocationOverlay = MyLocationNewOverlay(myLocationProvider, map)
        myLocationOverlay.enableMyLocation()
        myLocationOverlay.enableFollowLocation()
        myLocationOverlay.runOnFirstFix {
            map.overlays.clear()
            map.overlays.add(polyLine)
            map.overlays.add(myLocationOverlay)
        }
    }

    private fun centerLocation() {
        binding.map.controller.animateTo(myLocationOverlay.myLocation)
        myLocationOverlay.enableFollowLocation()
    }

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

    private fun checkLocationPermission() {
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

    private fun checkLocationEnabled() {
        val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isEnabled) {
            DialogManager.showLocationEnableDialog(
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

    private fun startStopService() {
        if (!LocationService.isRunning) {
            startService()
        } else stopService()
    }

    private fun startService() {
        mapViewModel.updateProgress(View.VISIBLE)
        startLocationService()
        if (orderViewModel.isOrderStarted.value == false) {
            orderViewModel.activeOrder()
        }
    }

    private fun stopService() {
        activity?.stopService(Intent(activity, LocationService::class.java))
        binding.ivStartStop.setImageResource(R.drawable.ic_play)
        mapViewModel.stopTracking()
        val track = getTrackItem()
        val geoPointList = locationModel?.geoPointsList ?: arrayListOf()
        DialogManager.showRouteDialog(requireContext(),
            track, object : SimpleListener {
                override fun onClick() {
                    showToast(getString(R.string.track_saved))
                    tracksViewModel.insertTrack(track, geoPointList)
                }
            })
    }

    private fun startLocationService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity?.startForegroundService(Intent(activity, LocationService::class.java))
        } else {
            activity?.startService(Intent(activity, LocationService::class.java))
        }
        binding.ivStartStop.setImageResource(R.drawable.ic_stop)
        LocationService.startTime = System.currentTimeMillis()
        mapViewModel.startTracking(LocationService.startTime)
    }

    private fun checkServiceState() {
        if (LocationService.isRunning) {
            binding.ivStartStop.setImageResource(R.drawable.ic_stop)
            mapViewModel.startTracking(LocationService.startTime)
        }
    }

    private fun setOnClicks() = with(binding) {
        val listener = onClicks()
        ivStartStop.setOnClickListener(listener)
        ivCenter.setOnClickListener(listener)
        btnSetPrice.setOnClickListener(listener)
    }

    private fun onClicks(): View.OnClickListener {
        return View.OnClickListener {
            when (it.id) {
                R.id.ivStartStop -> startStopService()
                R.id.ivCenter -> centerLocation()
                R.id.btnSetPrice -> setPrice()
            }
        }
    }

    private fun updateTime() {
        mapViewModel.timeData.observe(viewLifecycleOwner) {
            binding.tvTime.text = it
        }
    }

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

    private fun loadCurrentPrice() {
        val currentPrice = LocationService.startPrice
        binding.btnSetPrice.text = getString(R.string.price_format, currentPrice)
    }

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
                mapViewModel.updateLocation(locationModel!!)
            } else if (intent?.action == LocationService.PROGRESS_INTENT) {
                mapViewModel.updateProgress(View.GONE)
            }
        }
    }

    private fun registerLocationReceiver() {
        val locationFilter = IntentFilter().apply {
            addAction(LocationService.LOC_MODEL_INTENT)
            addAction(LocationService.PROGRESS_INTENT)
        }
        LocalBroadcastManager.getInstance(activity as AppCompatActivity)
            .registerReceiver(receiver, locationFilter)
    }

    private fun locationUpdates() = with(binding) {
        mapViewModel.locationUpdates.observe(viewLifecycleOwner) {
            val distance = "${getString(R.string.distance)} ${String.format(Locale.US, "%.1f", it.distance / 1000)} ${getString(R.string.km)}"
            val speed = "${getString(R.string.speed)} ${String.format(Locale.US, "%.1f", 3.6f * it.speed)} ${getString(R.string.km_h)}"
            val averageSpeed = "${getString(R.string.average_speed)} ${mapViewModel.getAverageSpeed(it.distance)} ${getString(R.string.km_h)}"
            val price = "${getString(R.string.price)} ${mapViewModel.getPrice(it.distance, LocationService.startPrice)} ${getString(R.string.currency_symbol)}"
            tvDistance.text = distance
            tvSpeed.text = speed
            tvAverageSpeed.text = averageSpeed
            tvPrice.text = price
            locationModel = it
            updatePolyline(it.geoPointsList)
        }
    }

    private fun addPoint(list: List<GeoPoint>) {
        if (list.isNotEmpty()) polyLine?.addPoint(list[list.size - 1])
    }

    private fun fillPolyline(list: List<GeoPoint>) {
        list.forEach {
            polyLine?.addPoint(it)
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

    private fun getTrackItem(): TrackItem {
        return TrackItem(
            null,
            mapViewModel.timeData.value ?: "00:00:00",
            TimeUtils.getDate(),
            String.format(Locale.US, "%.1f", (locationModel?.distance?.div(1000.0) ?: 0.0)),
            mapViewModel.getAverageSpeed(locationModel?.distance ?: 0.0f),
            geoPointsToString(locationModel?.geoPointsList ?: listOf()),
            mapViewModel.getPrice(locationModel?.distance ?: 0.0f, LocationService.startPrice),
            null,
            null
        )
    }

    private fun geoPointsToString(list: List<GeoPoint>): String {
        val stringBuilder = StringBuilder()
        list.forEach {
            stringBuilder.append("${it.latitude}, ${it.longitude}/")
        }
        return stringBuilder.toString()
    }

    private fun observeOrderFinish() {
        orderViewModel.isOrderFinished.observe(viewLifecycleOwner) { isOrderFinished ->
            if (isOrderFinished) {
                stopService()
                orderViewModel.updateFinishOrder(false)
            }
        }
    }

    private fun observeOrderState() {
        orderViewModel.isOrderFinished.observe(viewLifecycleOwner) {
            mainViewModel.changeBottomNavigation(CurrentScreen.MAP)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = MapFragment()
        const val KEY_COLOR = "color_key"
    }
}
