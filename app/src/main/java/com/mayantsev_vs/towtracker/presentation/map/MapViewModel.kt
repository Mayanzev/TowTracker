package com.mayantsev_vs.towtracker.presentation.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mayantsev_vs.towtracker.data.location.LocationModel

class MapViewModel() : ViewModel() {
    val locationUpdates = MutableLiveData<LocationModel>()
    val timeData = MutableLiveData<String>()
}
