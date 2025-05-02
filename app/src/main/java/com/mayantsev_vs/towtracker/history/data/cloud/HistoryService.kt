package com.mayantsev_vs.towtracker.history.data.cloud

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface HistoryService {
    @GET("order/get")
    suspend fun getHistory(@Header("Bearer-Authorization") token: String): OrderListResponseDTO

    @POST("/order/insert")
    suspend fun postHistory(@Header("Bearer-Authorization") token: String, @Body orderRequestDTO: OrderRequestDTO)
}