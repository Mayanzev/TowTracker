package com.mayantsev_vs.towtracker.service.data.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {
    @Insert
    suspend fun insertService(service: ServiceDBO)
    @Query("SELECT * FROM SERVICE")
    fun getAllServices(): Flow<List<ServiceDBO>>
    @Delete
    suspend fun deleteService(serviceDBO: ServiceDBO)
    @Query("DELETE FROM SERVICE")
    suspend fun deleteAllServices()
    @Query("SELECT * FROM SERVICE")
    fun getAllServicesPrint(): List<ServiceDBO>
}