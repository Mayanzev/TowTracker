package com.mayantsev_vs.towtracker.map.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mayantsev_vs.towtracker.map.data.location.LocationModel

class MapViewModel() : ViewModel() {
    val locationUpdates = MutableLiveData<LocationModel>()
    val timeData = MutableLiveData<String>()

    val _progressLiveData = MutableLiveData<Int>()
    val progressLiveData: LiveData<Int> = _progressLiveData

    fun updateProgress(value: Int) {
        _progressLiveData.value = value
    }

}
