package com.mayantsev_vs.towtracker.userProfile.data.cloud

data class UsernameResponseDTO(
    val login: String,
    val username: String
)

data class UsernameRequestDTO(
    val login: String,
    val username: String
)

data class PasswordRequestDTO(
    val login: String,
    val password: String,
    val newPassword: String
)