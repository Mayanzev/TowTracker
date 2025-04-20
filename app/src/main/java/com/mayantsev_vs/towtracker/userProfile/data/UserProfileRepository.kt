package com.mayantsev_vs.towtracker.userProfile.data


import com.mayantsev_vs.towtracker.login.data.cache.UserDao
import com.mayantsev_vs.towtracker.userProfile.data.cloud.PasswordReceive
import com.mayantsev_vs.towtracker.userProfile.data.cloud.UserProfileService
import com.mayantsev_vs.towtracker.userProfile.data.cloud.UsernameReceive
import retrofit2.HttpException
import java.net.ConnectException

class UserProfileRepository (
    private val userProfileService: UserProfileService,
    private val dao: UserDao
) {
    suspend fun clearUser() {
        return dao.clearUser()
    }

    suspend fun getUser(): UserProfileResult {
        try {
            val userCloud = userProfileService.fetchUser(dao.getToken() ?: "")
            return UserProfileResult.SuccessUser(
                userCloud.login,
                userCloud.username
            )
        } catch (_: ConnectException) {
            return UserProfileResult.Failure("Нет соединения с интернетом")
        } catch (e: HttpException) {
            return UserProfileResult.Failure(e.response()?.errorBody()?.string() ?: "")
        } catch (_: Exception) {
            return UserProfileResult.Failure("Ошибка соединения с сервером")
        }
    }

    suspend fun updateUser(email: String, username: String) {
        val userReceive = UsernameReceive(
            login = email,
            username = username
        )
        userProfileService.updateUser(dao.getToken() ?: "", userReceive)
    }

    suspend fun updateUserPassword(email: String, password: String, newPassword: String): UserProfileResult {
        try {
            val userPasswordReceive = PasswordReceive(
                login = email,
                password = password,
                newPassword = newPassword
            )
            userProfileService.updateUserPassword(dao.getToken() ?: "", userPasswordReceive)
            return UserProfileResult.Success
        } catch (_: ConnectException) {
            return UserProfileResult.Failure("Нет соединения с интернетом")
        } catch (e: HttpException) {
            return UserProfileResult.Failure(e.response()?.errorBody()?.string() ?: "")
        }
    }
}