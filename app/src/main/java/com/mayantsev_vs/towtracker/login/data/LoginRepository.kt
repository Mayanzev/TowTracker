package com.mayantsev_vs.towtracker.login.data

import com.mayantsev_vs.towtracker.data.db.Dao
import com.mayantsev_vs.towtracker.login.data.cache.UserItem
import com.mayantsev_vs.towtracker.login.data.cloud.LoginBody
import com.mayantsev_vs.towtracker.login.data.cloud.LoginService
import com.mayantsev_vs.towtracker.login.data.cloud.RegistrationBody

class LoginRepository(
    private val loginService: LoginService,
    private val dao: Dao
) {
    suspend fun login(email: String, password: String) {
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
    }

    suspend fun register(email: String, username: String, password: String) {
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
    }

    suspend fun getToken(): String? {
        return dao.getToken()
    }
}