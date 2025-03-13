package com.mayantsev_vs.towtracker.login.data

import android.util.Log

class LoginRepository (
    private val loginService: LoginService
) {
    suspend fun login(email: String, password: String) {
        val loginBody = LoginBody(
            email, password
        )
        val token = loginService.login(loginBody)
        Log.d("MyLog", token.token)
    }

    suspend fun register(email: String, username: String, password: String) {
        val registrationBody = RegistrationBody(
            email, password, username
        )
        val token = loginService.register(registrationBody)
        Log.d("MyLog", token.token)
    }
}