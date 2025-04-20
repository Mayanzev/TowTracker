package com.mayantsev_vs.towtracker.userProfile.data.cloud

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