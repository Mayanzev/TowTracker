package com.mayantsev_vs.towtracker.data.cloud

import retrofit2.http.GET
import retrofit2.http.Query

interface NominatimService {
    @GET("reverse")
    suspend fun reverseGeocode(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("format") format: String = "json",
    ): GeocodeResponse
}