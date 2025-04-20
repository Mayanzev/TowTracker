package com.mayantsev_vs.towtracker.auth.data.cloud

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("register")
    suspend fun register(@Body registrationBody: RegistrationBody): RegistrationResponse

    @POST("login")
    suspend fun login(@Body loginBody: LoginBody): LoginResponse
}