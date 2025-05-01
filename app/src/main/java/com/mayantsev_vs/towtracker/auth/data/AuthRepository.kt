package com.mayantsev_vs.towtracker.auth.data

import com.mayantsev_vs.towtracker.auth.data.cache.AuthDao
import com.mayantsev_vs.towtracker.auth.data.cache.AuthDBO
import com.mayantsev_vs.towtracker.auth.data.cloud.LoginRequestDTO
import com.mayantsev_vs.towtracker.auth.data.cloud.AuthService
import com.mayantsev_vs.towtracker.auth.data.cloud.RegistrationRequestDTO
import retrofit2.HttpException
import java.net.ConnectException

class AuthRepository(
    private val loginService: AuthService,
    private val dao: AuthDao
) {
    suspend fun login(email: String, password: String): AuthResult {
        try {
            val loginRequestDTO = LoginRequestDTO(
                email, password
            )
            val token = loginService.login(loginRequestDTO).token
            val userItem = AuthDBO(
                token
            )
            dao.insertUser(userItem)
            return AuthResult.Success
        } catch (_: ConnectException) {
            return AuthResult.Failure("Нет соединения с интернетом")
        } catch (e: HttpException) {
            return AuthResult.Failure(e.response()?.errorBody()?.string() ?: "")
        } catch (_: Exception) {
            return AuthResult.Failure("Ошибка соединения с сервером")
        }
    }

    suspend fun register(email: String, username: String, password: String): AuthResult {
        try {
            val registrationRequestDTO = RegistrationRequestDTO(
                email, password, username
            )
            val token = loginService.register(registrationRequestDTO).token
            val userItem = AuthDBO(
                token
            )
            dao.insertUser(userItem)
            return AuthResult.Success
        } catch (_: ConnectException) {
            return AuthResult.Failure("Нет соединения с интернетом")
        } catch (e: HttpException) {
            return AuthResult.Failure(e.response()?.errorBody()?.string() ?: "")
        } catch (_: Exception) {
            return AuthResult.Failure("Ошибка соединения с сервером")
        }
    }

    suspend fun getToken(): String? {
        return dao.getToken()
    }
}