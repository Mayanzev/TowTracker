package com.mayantsev_vs.towtracker.login.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserItem)

    @Query("SELECT * FROM users_table WHERE login = (:login)")
    suspend fun getUser(login: String): UserItem

    @Query("SELECT token FROM users_table")
    suspend fun getToken(): String?

    @Query("DELETE FROM users_table")
    suspend fun clearUser()

    @Query("SELECT username FROM users_table")
    suspend fun getUsername(): String?
}