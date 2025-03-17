package com.mayantsev_vs.towtracker.login.presentation

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayantsev_vs.towtracker.login.data.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.mayantsev_vs.towtracker.login.data.Result

class LoginViewModel(
    private val repository: LoginRepository
) : ViewModel() {

    private val _stateLiveData: MutableLiveData<UiState> = MutableLiveData()
    val stateLiveData: LiveData<UiState> = _stateLiveData

    private val _navigationLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val navigationLiveData: LiveData<Boolean> = _navigationLiveData

    private val _registeredLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val registeredLiveData: LiveData<Boolean> = _registeredLiveData

    private val _userLiveData: MutableLiveData<UserUiItem> = MutableLiveData()
    val userLiveData: LiveData<UserUiItem> = _userLiveData

    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: MutableLiveData<String> = _error

    fun register(email: String, username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.register(email, username, password)
            if (result == Result.Success) {
                _navigationLiveData.postValue(true)
                _error.postValue("")
            }
            else {
                val failure = result as Result.Failure
                _error.postValue(failure.message)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.login(email, password)
            if (result == Result.Success) {
                _navigationLiveData.postValue(true)
                _error.postValue("")
            } else {
                val failure = result as Result.Failure
                _error.postValue(failure.message)
            }
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
        _navigationLiveData.value = false
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearUser()
        }
    }

    fun getUser() {
        _error.value = ""
        viewModelScope.launch(Dispatchers.IO) {
            val userData = repository.getUser()
            if (userData is Result.SuccessUser) {
                _userLiveData.postValue(
                    UserUiItem(
                        userData.login,
                        userData.password,
                        userData.username
                    )
                )
            } else {
                val failure = userData as Result.Failure
                _error.postValue(failure.message)
            }
        }
    }
}