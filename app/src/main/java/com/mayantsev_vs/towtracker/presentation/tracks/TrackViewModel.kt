package com.mayantsev_vs.towtracker.presentation.tracks

import android.util.Log
import androidx.lifecycle.*
import com.mayantsev_vs.towtracker.data.cloud.Address
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

        val latitudeSecondCity = geoPointList.last().latitude
        val longitudeSecondCity = geoPointList.last().longitude
        try {

            Log.d("mylog", "$latitudeFirstCity, $longitudeFirstCity")
            Log.d("mylog", "$latitudeSecondCity, $longitudeSecondCity")

            val responseFirst = nominatimService.reverseGeocode(latitudeFirstCity, longitudeFirstCity)
            val responseSecond = nominatimService.reverseGeocode(latitudeSecondCity, longitudeSecondCity)

            val firstCityAddress = buildFullAddress(responseFirst.address)
            val secondCityAddress = buildFullAddress(responseSecond.address)

            Log.d("mylog", firstCityAddress)
            Log.d("mylog", secondCityAddress)

            newTrackItem = TrackItem(
                newTrackItem.id,
                newTrackItem.time,
                newTrackItem.date,
                newTrackItem.distance,
                newTrackItem.speed,
                newTrackItem.geoPoints,
                newTrackItem.price,
                firstCityAddress,
                secondCityAddress
            )
        } catch (e: Exception) {
        }

        dao.insertTrack(newTrackItem)
    }

    fun buildFullAddress(address: Address): String {
        val nonNullParts = listOfNotNull(
            address.city,
            address.town,
            address.village,
            address.road,
            address.houseNumber
        ).distinct()

        return nonNullParts.joinToString(", ")
    }

    fun deleteTrack(trackItem: TrackItem) = viewModelScope.launch {
        dao.deleteTrack(trackItem)
    }

    fun deleteAllTracks() = viewModelScope.launch {
        dao.deleteAllTracks()
    }
}