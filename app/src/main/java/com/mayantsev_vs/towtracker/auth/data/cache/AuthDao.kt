package com.mayantsev_vs.towtracker.auth.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AuthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: AuthDBO)

    @Query("SELECT token FROM auth_table")
    suspend fun getToken(): String?

    @Query("DELETE FROM auth_table")
    suspend fun clearUser()
}