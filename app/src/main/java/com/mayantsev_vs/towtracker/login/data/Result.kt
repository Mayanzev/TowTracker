package com.mayantsev_vs.towtracker.login.data

sealed class Result {

    data object Success : Result()

    data class Failure(val message: String) : Result()

    data class SuccessUser(
        val login: String,
        val username: String
    ) : Result()

}