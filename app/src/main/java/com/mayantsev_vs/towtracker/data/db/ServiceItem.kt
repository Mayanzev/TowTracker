package com.mayantsev_vs.towtracker.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "service")
data class ServiceItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo (name = "price")
    val price: String,
    @ColumnInfo (name = "date")
    val date: String
)
