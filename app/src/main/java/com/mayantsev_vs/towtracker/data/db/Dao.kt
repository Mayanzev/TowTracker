package com.mayantsev_vs.towtracker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackItem)
    @Query("SELECT * FROM TRACK")
    fun getAllTracks(): Flow<List<TrackItem>>
    @Delete
    suspend fun deleteTrack(trackItem: TrackItem)
    @Query("DELETE FROM TRACK")
    suspend fun deleteAllTracks()

    @Insert
    suspend fun insertService(service: ServiceItem)
    @Query("SELECT * FROM SERVICE")
    fun getAllServices(): Flow<List<ServiceItem>>
    @Delete
    suspend fun deleteService(serviceItem: ServiceItem)
    @Query("DELETE FROM SERVICE")
    suspend fun deleteAllServices()
}