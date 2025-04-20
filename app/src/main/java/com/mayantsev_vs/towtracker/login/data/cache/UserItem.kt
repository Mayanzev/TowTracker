package com.mayantsev_vs.towtracker.login.data.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class UserItem(
    @PrimaryKey @ColumnInfo(name = "token") val token: String
)