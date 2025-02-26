package com.mayantsev_vs.towtracker.presentation.tracks

import android.util.Log
import androidx.lifecycle.*
import com.mayantsev_vs.towtracker.data.cloud.NominatimService
import com.mayantsev_vs.towtracker.data.db.MainDb
import com.mayantsev_vs.towtracker.data.db.TrackItem
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

class TrackViewModel(db: MainDb, private val nominatimService: NominatimService) : ViewModel() {

    private val dao = db.getDao()
    val tracks = dao.getAllTracks().asLiveData()
    val currentTrack = MutableLiveData<TrackItem>()

    fun insertTrack(trackItem: TrackItem, geoPointList: ArrayList<GeoPoint>) = viewModelScope.launch {

        var newTrackItem = trackItem
        val latitudeFirstCity = geoPointList.first().latitude
        val longitudeFirstCity = geoPointList.first().longitude
        try {
            Log.d("mylog", "$latitudeFirstCity, $longitudeFirstCity")
            val response = nominatimService.reverseGeocode(latitudeFirstCity, longitudeFirstCity)
            val firstCity = response.address.city ?: "Неизвестно"
            newTrackItem = TrackItem(
                newTrackItem.id,
                newTrackItem.time,
                newTrackItem.date,
                newTrackItem.distance,
                newTrackItem.speed,
                newTrackItem.geoPoints,
                newTrackItem.price,
                firstCity
            )
        } catch (e: Exception) {
        }

        dao.insertTrack(newTrackItem)
    }

    fun deleteTrack(trackItem: TrackItem) = viewModelScope.launch {
        dao.deleteTrack(trackItem)
    }

    fun deleteAllTracks() = viewModelScope.launch {
        dao.deleteAllTracks()
    }
}