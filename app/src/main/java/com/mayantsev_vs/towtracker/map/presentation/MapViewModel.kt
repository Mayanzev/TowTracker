package com.mayantsev_vs.towtracker.map.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mayantsev_vs.towtracker.main.utils.TimeUtils
import com.mayantsev_vs.towtracker.map.data.location.LocationModel
import java.math.BigDecimal
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class MapViewModel : ViewModel() {

    val locationUpdates = MutableLiveData<LocationModel>()
    val timeData = MutableLiveData<String>()
    val isTracking = MutableLiveData<Boolean>()

    private var startTime = 0L
    private var timer: Timer? = null

    private val _progressLiveData = MutableLiveData<Int>()
    val progressLiveData: LiveData<Int> = _progressLiveData

    fun startTracking(startTimeFromService: Long) {
        startTime = startTimeFromService
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                timeData.postValue(getFormattedElapsedTime())
            }
        }, 1000, 1000)
        isTracking.value = true
    }

    fun stopTracking() {
        timer?.cancel()
        timer = null
        isTracking.value = false
    }

    private fun getFormattedElapsedTime(): String {
        val elapsed = System.currentTimeMillis() - startTime
        return TimeUtils.getTime(elapsed)
    }

    fun updateLocation(locationModel: LocationModel) {
        locationUpdates.value = locationModel
    }

    fun updateProgress(value: Int) {
        _progressLiveData.value = value
    }

    fun getAverageSpeed(distance: Float): String {
        if (isTracking.value != true) return "0.0"
        val elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0f
        return if (elapsedTime > 0) {
            String.format(Locale.US, "%.1f", 3.6f * (distance / elapsedTime))
        } else {
            "0.0"
        }
    }

    fun getPrice(distance: Float, startPrice: BigDecimal): String {
        val distanceInKm = BigDecimal(distance / 1000.0)
        val price = distanceInKm.multiply(startPrice)
        return String.format(Locale.US, "%.1f", price)
    }
}
