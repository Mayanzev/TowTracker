package com.mayantsev_vs.towtracker.login.data

import com.mayantsev_vs.towtracker.login.data.cache.UserDao
import com.mayantsev_vs.towtracker.login.data.cache.UserItem
import com.mayantsev_vs.towtracker.login.data.cloud.LoginBody
import com.mayantsev_vs.towtracker.login.data.cloud.LoginService
import com.mayantsev_vs.towtracker.login.data.cloud.PasswordReceive
import com.mayantsev_vs.towtracker.login.data.cloud.RegistrationBody
import com.mayantsev_vs.towtracker.login.data.cloud.UsernameReceive
import retrofit2.HttpException
import java.net.ConnectException

class LoginRepository(
    private val loginService: LoginService,
    private val dao: UserDao
) {
    suspend fun login(email: String, password: String): Result {
        try {
            val loginBody = LoginBody(
                email, password
            )
            val token = loginService.login(loginBody).token
            val userItem = UserItem(
                email,
                "",
                token,
                password
            )
            dao.insertUser(userItem)
            return Result.Success
        } catch (_: ConnectException) {
            return Result.Failure("Нет соединения с интернетом")
        } catch (e: HttpException) {
            return Result.Failure(e.response()?.errorBody()?.string() ?: "")
        } catch (_: Exception) {
            return Result.Failure("Ошибка соединения с сервером")
        }
    }

    suspend fun register(email: String, username: String, password: String): Result {
        try {
            val registrationBody = RegistrationBody(
                email, password, username
            )
            val token = loginService.register(registrationBody).token
            val userItem = UserItem(
                email,
                username,
                token,
                password
            )
            dao.insertUser(userItem)
            return Result.Success
        } catch (_: ConnectException) {
            return Result.Failure("Нет соединения с интернетом")
        } catch (e: HttpException) {
            return Result.Failure(e.response()?.errorBody()?.string() ?: "")
        } catch (_: Exception) {
            return Result.Failure("Ошибка соединения с сервером")
        }
    }

    suspend fun getToken(): String? {
        return dao.getToken()
    }

    suspend fun clearUser() {
        return dao.clearUser()
    }

    suspend fun getUser(): Result {
        try {
            val userCloud = loginService.fetchUser(dao.getToken() ?: "")
            return Result.SuccessUser(
                userCloud.login,
                userCloud.username
            )
        } catch (_: ConnectException) {
            return Result.Failure("Нет соединения с интернетом")
        } catch (e: HttpException) {
            return Result.Failure(e.response()?.errorBody()?.string() ?: "")
        } catch (_: Exception) {
            return Result.Failure("Ошибка соединения с сервером")
        }
    }

    suspend fun updateUser(email: String, username: String) {
        val userReceive = UsernameReceive(
            login = email,
            username = username
        )
        loginService.updateUser(dao.getToken() ?: "", userReceive)
    }

    suspend fun updateUserPassword(email: String, password: String, newPassword: String): Result {
        try {
            val userPasswordReceive = PasswordReceive(
                login = email,
                password = password,
                newPassword = newPassword
            )
            loginService.updateUserPassword(dao.getToken() ?: "", userPasswordReceive)
            return Result.Success
        } catch (_: ConnectException) {
            return Result.Failure("Нет соединения с интернетом")
        } catch (e: HttpException) {
            return Result.Failure(e.response()?.errorBody()?.string() ?: "")
        }
    }
}