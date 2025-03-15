package com.mayantsev_vs.towtracker.login.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayantsev_vs.towtracker.login.data.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val repository: LoginRepository
) : ViewModel() {

    private val _stateLiveData: MutableLiveData<UiState> = MutableLiveData()
    val stateLiveData: LiveData<UiState> = _stateLiveData

    private val _navigationLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val navigationLiveData: LiveData<Boolean> = _navigationLiveData

    private val _registeredLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val registeredLiveData: LiveData<Boolean> = _registeredLiveData

    fun register(email: String, username: String, password: String, repeatedPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.register(email, username, password)
            _navigationLiveData.postValue(true)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.login(email, password)
            _navigationLiveData.postValue(true)
        }
    }

    fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            val token = repository.getToken()
            _registeredLiveData.postValue(token != null)
        }
    }

    fun updateRegistered(isLogin: Boolean) {
        if (isLogin) {
            _stateLiveData.value = UiState.Login
        } else {
            _stateLiveData.value = UiState.Register
        }
    }

    fun clearUser() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearUser()
        }
    }

}