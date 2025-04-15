package com.mayantsev_vs.towtracker.history.data.cloud

import retrofit2.http.GET
import retrofit2.http.Header

interface HistoryService {
    @GET("order/get")
    suspend fun getHistory(@Header("Bearer-Authorization") token: String): OrderListCloud
}