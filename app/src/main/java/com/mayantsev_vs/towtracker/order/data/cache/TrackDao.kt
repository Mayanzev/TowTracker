package com.mayantsev_vs.towtracker.order.data.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackItem)
    @Query("SELECT * FROM TRACK")
    fun getAllTracks(): Flow<List<TrackItem>>
    @Delete
    suspend fun deleteTrack(trackItem: TrackItem)
    @Query("DELETE FROM TRACK")
    suspend fun deleteAllTracks()
    @Query("SELECT * FROM TRACK")
    fun getAllTracksList(): List<TrackItem>
}