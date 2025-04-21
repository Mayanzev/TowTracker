package com.mayantsev_vs.towtracker.sl

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mayantsev_vs.towtracker.auth.data.cache.AuthDao
import com.mayantsev_vs.towtracker.auth.data.cache.AuthItem
import com.mayantsev_vs.towtracker.service.data.cache.ServiceDao
import com.mayantsev_vs.towtracker.service.data.cache.ServiceItem
import com.mayantsev_vs.towtracker.track.data.cache.TrackDao
import com.mayantsev_vs.towtracker.track.data.cache.TrackItem

@Database(entities = [TrackItem::class, ServiceItem::class, AuthItem::class], version = 1)
abstract class MainDb : RoomDatabase() {

    abstract fun getDaoAuth(): AuthDao
    abstract fun getDaoTrack(): TrackDao
    abstract fun getDaoService(): ServiceDao

    companion object {
        @Volatile
        var INSTANCE: MainDb? = null
        fun getDatabase(context: Context): MainDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDb::class.java,
                    "TowTracker.db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}