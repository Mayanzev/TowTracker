package com.mayantsev_vs.towtracker.db

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface Dao {
    @Insert
    suspend fun insertTrack(track: TrackItem)
}