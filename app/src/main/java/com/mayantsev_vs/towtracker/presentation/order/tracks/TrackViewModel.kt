package com.mayantsev_vs.towtracker.presentation.order.tracks

import androidx.lifecycle.*
import com.mayantsev_vs.towtracker.data.db.MainDb
import com.mayantsev_vs.towtracker.data.db.TrackItem
import kotlinx.coroutines.launch

class TrackViewModel(db: MainDb) : ViewModel() {

    private val dao = db.getDao()
    val tracks = dao.getAllTracks().asLiveData()
    val currentTrack = MutableLiveData<TrackItem>()

    fun insertTrack(trackItem: TrackItem) = viewModelScope.launch {
        dao.insertTrack(trackItem)
    }

    fun deleteTrack(trackItem: TrackItem) = viewModelScope.launch {
        dao.deleteTrack(trackItem)
    }

    fun deleteAllTracks() = viewModelScope.launch {
        dao.deleteAllTracks()
    }
}