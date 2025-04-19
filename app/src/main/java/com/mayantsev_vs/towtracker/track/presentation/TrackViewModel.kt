package com.mayantsev_vs.towtracker.track.presentation

import androidx.lifecycle.*
import com.mayantsev_vs.towtracker.track.data.cache.TrackItem
import com.mayantsev_vs.towtracker.order.data.cloud.Address
import com.mayantsev_vs.towtracker.order.data.cloud.NominatimService
import com.mayantsev_vs.towtracker.sl.MainDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

class TrackViewModel(db: MainDb, private val nominatimService: NominatimService) : ViewModel() {

    private val dao = db.getDaoTrack()
    val tracks = dao.getAllTracks().asLiveData()
    val currentTrack = MutableLiveData<TrackItem>()
    private val _swipeLiveData = MutableLiveData<Boolean>()
    val swipeLiveData: LiveData<Boolean> = _swipeLiveData

    fun insertTrack(trackItem: TrackItem, geoPointList: ArrayList<GeoPoint>) =
        viewModelScope.launch {

            var newTrackItem = trackItem
            val latitudeFirstCity = geoPointList.first().latitude
            val longitudeFirstCity = geoPointList.first().longitude

            val latitudeSecondCity = geoPointList.last().latitude
            val longitudeSecondCity = geoPointList.last().longitude

            try {
                val responseFirst =
                    nominatimService.reverseGeocode(latitudeFirstCity, longitudeFirstCity)
                val responseSecond =
                    nominatimService.reverseGeocode(latitudeSecondCity, longitudeSecondCity)

                val firstCityAddress = buildFullAddress(responseFirst.address)
                val secondCityAddress = buildFullAddress(responseSecond.address)

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
            } catch (_: Exception) {
                val errorMessage = "Не удалось получить адрес. Проверьте интернет-соединение."
                newTrackItem = TrackItem(
                    newTrackItem.id,
                    newTrackItem.time,
                    newTrackItem.date,
                    newTrackItem.distance,
                    newTrackItem.speed,
                    newTrackItem.geoPoints,
                    newTrackItem.price,
                    errorMessage,
                    errorMessage
                )
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

    fun updateErrorTracks() = viewModelScope.launch(Dispatchers.IO) {
        _swipeLiveData.postValue(true)
        dao.getAllTracksList().filter {
            it.firstCity == "Не удалось получить адрес. Проверьте интернет-соединение." ||
                    it.secondCity == "Не удалось получить адрес. Проверьте интернет-соединение."
        }.forEach {
            var newTrackItem = it

            val geoPointsPair = getCoordinates(it.geoPoints)
            val latitudeFirstCity = geoPointsPair.first.first
            val longitudeFirstCity = geoPointsPair.first.second
            val latitudeSecondCity = geoPointsPair.second.first
            val longitudeSecondCity = geoPointsPair.second.second

            try {
                val responseFirst =
                    nominatimService.reverseGeocode(latitudeFirstCity, longitudeFirstCity)
                val responseSecond =
                    nominatimService.reverseGeocode(latitudeSecondCity, longitudeSecondCity)

                val firstCityAddress = buildFullAddress(responseFirst.address)
                val secondCityAddress = buildFullAddress(responseSecond.address)

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
            } catch (_: Exception) {
                val errorMessage = "Не удалось получить адрес. Проверьте интернет-соединение."
                newTrackItem = TrackItem(
                    newTrackItem.id,
                    newTrackItem.time,
                    newTrackItem.date,
                    newTrackItem.distance,
                    newTrackItem.speed,
                    newTrackItem.geoPoints,
                    newTrackItem.price,
                    errorMessage,
                    errorMessage
                )
            }
            dao.insertTrack(newTrackItem)
        }
        _swipeLiveData.postValue(false)
    }

    private fun getCoordinates(geoPoints: String): Pair<Pair<Double, Double>, Pair<Double, Double>> {
        val list = geoPoints.dropLast(1).split('/')
        val firstCoordinates = list.first().split(", ")
        val secondCoordinates = list.last().split(", ")

        val firstCityPair =
            Pair(firstCoordinates.first().toDouble(), firstCoordinates.last().toDouble())
        val secondCityPair =
            Pair(secondCoordinates.first().toDouble(), secondCoordinates.last().toDouble())

        return Pair(firstCityPair, secondCityPair)
    }
}