package com.mayantsev_vs.towtracker.userProfile.data.cloud

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserProfileService {
    @GET("user/fetch")
    suspend fun fetchUser(@Header("Bearer-Authorization") token: String): UsernameResponse

    @POST("user/update/username")
    suspend fun updateUser(@Header("Bearer-Authorization") token: String, @Body user: UsernameReceive)

    @POST("user/update/password")
    suspend fun updateUserPassword(@Header("Bearer-Authorization") token: String, @Body user: PasswordReceive)
}