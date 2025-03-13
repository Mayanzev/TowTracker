package com.mayantsev_vs.towtracker.login.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayantsev_vs.towtracker.login.data.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: LoginRepository
): ViewModel() {

    private val _stateLiveData: MutableLiveData<UiState> = MutableLiveData()
    val stateLiveData: LiveData<UiState> = _stateLiveData

    fun register(email: String, username: String, password: String, repeatedPassword: String) {
        viewModelScope.launch {
            repository.register(email, username, password)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            repository.login(email, password)
        }
    }

    fun updateRegistered(isLogin: Boolean) {
        if (isLogin) {
            _stateLiveData.value = UiState.Login
        } else {
            _stateLiveData.value = UiState.Register
        }
    }

}