package com.mayantsev_vs.towtracker.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mayantsev_vs.towtracker.data.db.MainDb
import com.mayantsev_vs.towtracker.data.db.ServiceItem
import com.mayantsev_vs.towtracker.data.db.TrackItem
import com.mayantsev_vs.towtracker.data.location.LocationModel
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class MainViewModel(db: MainDb) : ViewModel() {

    val dao = db.getDao()
    val locationUpdates = MutableLiveData<LocationModel>()
    val timeData = MutableLiveData<String>()
    val currentTrack = MutableLiveData<TrackItem>()
    val tracks = dao.getAllTracks().asLiveData()
    val services = dao.getAllServices().asLiveData()

    // factory for creating instances of ViewModel with a database dependency
    class ViewModelFactory(private val db: MainDb) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(db) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

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

}