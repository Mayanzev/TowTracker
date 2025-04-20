package com.mayantsev_vs.towtracker.login.data

import com.mayantsev_vs.towtracker.login.data.cache.UserDao
import com.mayantsev_vs.towtracker.login.data.cache.UserItem
import com.mayantsev_vs.towtracker.login.data.cloud.LoginBody
import com.mayantsev_vs.towtracker.login.data.cloud.LoginService
import com.mayantsev_vs.towtracker.login.data.cloud.RegistrationBody
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
                token
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
                token
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
}