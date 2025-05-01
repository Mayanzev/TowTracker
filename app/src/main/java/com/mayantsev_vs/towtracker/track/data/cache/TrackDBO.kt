package com.mayantsev_vs.towtracker.track.data.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track")
data class TrackDBO(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "time")
    val time: String,
    @ColumnInfo (name = "date")
    val date: String,
    @ColumnInfo (name = "distance")
    val distance: String,
    @ColumnInfo (name = "speed")
    val speed: String,
    @ColumnInfo (name = "geo_points")
    val geoPoints: String,
    @ColumnInfo (name = "price")
    val price: String,
    @ColumnInfo (name = "firstCity")
    val firstCity: String?,
    @ColumnInfo (name = "secondCity")
    val secondCity: String?
)
