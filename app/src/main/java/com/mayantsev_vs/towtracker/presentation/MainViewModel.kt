package com.mayantsev_vs.towtracker.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mayantsev_vs.towtracker.data.db.MainDb
import com.mayantsev_vs.towtracker.data.db.ServiceItem
import com.mayantsev_vs.towtracker.data.db.TrackItem
import com.mayantsev_vs.towtracker.data.location.LocationModel
import com.mayantsev_vs.towtracker.data.utils.PreferencesHelper
import kotlinx.coroutines.launch

class MainViewModel(db: MainDb, private val preferencesHelper: PreferencesHelper) : ViewModel() {

    val dao = db.getDao()
    val locationUpdates = MutableLiveData<LocationModel>()
    val timeData = MutableLiveData<String>()
    val currentTrack = MutableLiveData<TrackItem>()
    val tracks = dao.getAllTracks().asLiveData()
    val services = dao.getAllServices().asLiveData()
    val isOrderStarted = MutableLiveData<Boolean>()

    fun insertTrack(trackItem: TrackItem) = viewModelScope.launch {
        dao.insertTrack(trackItem)
    }

    fun deleteTrack(trackItem: TrackItem) = viewModelScope.launch {
        dao.deleteTrack(trackItem)
    }

    fun insertService(serviceItem: ServiceItem) = viewModelScope.launch {
        dao.insertService(serviceItem)
    }

    fun deleteService(serviceItem: ServiceItem) = viewModelScope.launch {
        dao.deleteService(serviceItem)
    }

    fun deleteAllData() = viewModelScope.launch {
        dao.deleteAllTracks()
        dao.deleteAllServices()
    }


    init {
        loadOrderState()
    }

    private fun loadOrderState() {
        isOrderStarted.value = preferencesHelper.isOrderStarted
    }

    private fun saveOrderState(isStarted: Boolean) {
        preferencesHelper.isOrderStarted = isStarted
    }

    fun startNewOrder() {
        isOrderStarted.value = true
        saveOrderState(true)
    }

    fun finishOrder() {
        isOrderStarted.value = false
        saveOrderState(false)
    }
}