package com.mayantsev_vs.towtracker.order.data.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {
    @Insert
    suspend fun insertService(service: ServiceItem)
    @Query("SELECT * FROM SERVICE")
    fun getAllServices(): Flow<List<ServiceItem>>
    @Delete
    suspend fun deleteService(serviceItem: ServiceItem)
    @Query("DELETE FROM SERVICE")
    suspend fun deleteAllServices()
    @Query("SELECT * FROM SERVICE")
    fun getAllServicesPrint(): List<ServiceItem>
}