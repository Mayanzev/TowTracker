package com.mayantsev_vs.towtracker.auth.data.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auth_table")
data class AuthItem(
    @PrimaryKey @ColumnInfo(name = "token") val token: String
)