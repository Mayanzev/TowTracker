package com.mayantsev_vs.towtracker.userProfile.data

sealed class UserProfileResult {
    data object Success : UserProfileResult()
    data class Failure(val message: String) : UserProfileResult()
    data class SuccessUser(
        val login: String,
        val username: String
    ) : UserProfileResult()
}