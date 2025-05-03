package com.mayantsev_vs.towtracker.auth.data

sealed class AuthResult {
    data object Success : AuthResult()
    data class Failure(val message: String) : AuthResult()
}