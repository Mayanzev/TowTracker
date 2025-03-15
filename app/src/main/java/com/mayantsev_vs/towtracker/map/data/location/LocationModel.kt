package com.mayantsev_vs.towtracker.map.data.location

import org.osmdroid.util.GeoPoint
import java.io.Serializable

// A data class representing location data, including velocity, distance, and a list of geographical points.
// The class implements Serializable to allow passing instances between components.
data class LocationModel(
    val speed: Float = 0.0f,
    val distance: Float = 0.0f,
    val geoPointsList: ArrayList<GeoPoint>
) : Serializable
