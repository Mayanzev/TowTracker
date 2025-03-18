package com.mayantsev_vs.towtracker.login.data.cloud

data class RegistrationBody (
    val login: String,
    val password: String,
    val username: String
)

data class RegistrationResponse (
    val token: String
)

data class LoginBody(
    val login: String,
    val password: String
)

data class LoginResponse (
    val token: String
)

data class UsernameResponse(
    val login: String,
    val username: String
)

data class UsernameReceive(
    val login: String,
    val username: String
)

data class PasswordReceive(
    val login: String,
    val password: String,
    val newPassword: String
)