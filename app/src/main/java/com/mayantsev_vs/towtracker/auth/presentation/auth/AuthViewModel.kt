package com.mayantsev_vs.towtracker.auth.presentation.auth

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayantsev_vs.towtracker.auth.data.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.mayantsev_vs.towtracker.auth.data.AuthResult

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _stateLiveData: MutableLiveData<AuthUiState> = MutableLiveData()
    val stateLiveData: LiveData<AuthUiState> = _stateLiveData

    private val _navigationLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val navigationLiveData: LiveData<Boolean> = _navigationLiveData

    private val _registeredLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val registeredLiveData: LiveData<Boolean> = _registeredLiveData

    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: MutableLiveData<String> = _error

    val _progressLiveData = MutableLiveData<Int>()
    val progressLiveData: LiveData<Int> = _progressLiveData


    fun register(email: String, username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.register(email, username, password)
            if (result == AuthResult.Success) {
                _navigationLiveData.postValue(true)
                _error.postValue("")
            }
            else {
                val failure = result as AuthResult.Failure
                _error.postValue(failure.message)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.login(email, password)
            if (result == AuthResult.Success) {
                _navigationLiveData.postValue(true)
                _error.postValue("")
            } else {
                val failure = result as AuthResult.Failure
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
            _stateLiveData.value = AuthUiState.Login
        } else {
            _stateLiveData.value = AuthUiState.Register
        }
    }

    fun updateNavigation(value: Boolean) {
        _navigationLiveData.value = value
    }

}