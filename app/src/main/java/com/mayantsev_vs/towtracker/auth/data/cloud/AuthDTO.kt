package com.mayantsev_vs.towtracker.auth.data.cloud

data class RegistrationRequestDTO (
    val login: String,
    val password: String,
    val username: String
)

data class RegistrationResponseDTO (
    val token: String
)

data class LoginRequestDTO(
    val login: String,
    val password: String
)

data class LoginResponseDTO (
    val token: String
)