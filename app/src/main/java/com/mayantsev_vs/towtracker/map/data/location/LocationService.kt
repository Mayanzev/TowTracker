package com.mayantsev_vs.towtracker.map.data.location

import android.Manifest
import com.mayantsev_vs.towtracker.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.mayantsev_vs.towtracker.main.presentation.MainActivity
import org.osmdroid.util.GeoPoint
import java.math.BigDecimal

// service that is needed for the application to run in the background
class LocationService : Service() {
    private lateinit var locationProvider: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private var lastLocation: Location? = null
    private var distance = 0.0f
    private lateinit var geoPointsList: ArrayList<GeoPoint>
    private var isDebug = true // for development

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startNotification()
        startLocationUpdates()
        isRunning = true
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        geoPointsList = ArrayList()
        initLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        locationProvider.removeLocationUpdates(locCallBack)
    }

    // starts a foreground notification for the service with a custom notification channel and content.
    private fun startNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nChannel = NotificationChannel(
                CHANNEL_ID,
                "Location Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(nChannel)
        }
        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.mipmap.sym_def_app_icon)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_content))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT).build()

        startForeground(99, notification)
    }

    // function that initializes a class to get location information
    private fun initLocation() {
        val updateInterval = PreferenceManager.getDefaultSharedPreferences(
            this
        ).getString("update_time_key", "3000")?.toLong() ?: 3000
        locationRequest = LocationRequest.Builder(PRIORITY_HIGH_ACCURACY, updateInterval)
            .setMinUpdateIntervalMillis(updateInterval)
            .build()
        locationProvider = LocationServices.getFusedLocationProviderClient(baseContext)
    }

    // function whereby we begin to receive information about our location
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationProvider.requestLocationUpdates(
            locationRequest,
            locCallBack,
            Looper.myLooper()
        )
    }

    // locationCallback to receive location updates
    private val locCallBack = object  : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val currentLocation = locationResult.lastLocation

            if (lastLocation != null && currentLocation != null) {

                if (currentLocation.speed > 0.4 || isDebug) {
                    distance += lastLocation!!.distanceTo(currentLocation)
                    geoPointsList.add(GeoPoint(currentLocation.latitude, currentLocation.longitude))
                }

                val locationModel = LocationModel(
                    currentLocation.speed,
                    distance,
                    geoPointsList
                )
                sendLocationData(locationModel)
            }
            lastLocation = currentLocation
        }
    }

    // function to send location data via broadcast using LocalBroadcastManager
    private fun sendLocationData(locationModel: LocationModel) {
        val intent = Intent(LOC_MODEL_INTENT)
        intent.putExtra(LOC_MODEL_INTENT, locationModel)
        val broadcast = LocalBroadcastManager.getInstance(applicationContext).apply { sendBroadcast(intent) }

        val progressIntent = Intent(PROGRESS_INTENT)
        broadcast.sendBroadcast(progressIntent)
    }


    companion object {
        const val CHANNEL_ID = "channel_1"
        const val LOC_MODEL_INTENT = "loc_intent"
        const val PROGRESS_INTENT = "progress_intent"
        var isRunning = false
        var startTime = 0L
        var startPrice: BigDecimal = BigDecimal("0.0")
    }
}