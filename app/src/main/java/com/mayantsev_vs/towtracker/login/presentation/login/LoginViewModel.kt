package com.mayantsev_vs.towtracker.login.presentation.login

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayantsev_vs.towtracker.login.data.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.mayantsev_vs.towtracker.login.data.Result
import com.mayantsev_vs.towtracker.login.presentation.UserUiItem

class LoginViewModel(
    private val repository: LoginRepository
) : ViewModel() {

    private val _stateLiveData: MutableLiveData<LoginUiState> = MutableLiveData()
    val stateLiveData: LiveData<LoginUiState> = _stateLiveData

    private val _navigationLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val navigationLiveData: LiveData<Boolean> = _navigationLiveData

    private val _registeredLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val registeredLiveData: LiveData<Boolean> = _registeredLiveData

    private val _userLiveData: MutableLiveData<UserUiItem> = MutableLiveData()
    val userLiveData: LiveData<UserUiItem> = _userLiveData

    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: MutableLiveData<String> = _error

    val _progressLiveData = MutableLiveData<Int>()
    val progressLiveData: LiveData<Int> = _progressLiveData


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
        _progressLiveData.value = View.VISIBLE
        viewModelScope.launch(Dispatchers.IO) {
            val token = repository.getToken()
            _registeredLiveData.postValue(token != null)
            _progressLiveData.postValue(View.GONE)
        }
    }

    fun updateRegistered(isLogin: Boolean) {
        if (isLogin) {
            _stateLiveData.value = LoginUiState.Login
        } else {
            _stateLiveData.value = LoginUiState.Register
        }
    }

    fun clearUser() {
        _navigationLiveData.value = false
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearUser()
        }
    }

    fun getUser() {
        _progressLiveData.value = View.VISIBLE
        _error.value = ""
        viewModelScope.launch(Dispatchers.IO) {
            val userData = repository.getUser()
            if (userData is Result.SuccessUser) {
                _userLiveData.postValue(
                    UserUiItem(
                        userData.login,
                        userData.username
                    )
                )
            } else {
                val failure = userData as Result.Failure
                _error.postValue(failure.message)
            }
            _progressLiveData.postValue(View.GONE)
        }
    }
}