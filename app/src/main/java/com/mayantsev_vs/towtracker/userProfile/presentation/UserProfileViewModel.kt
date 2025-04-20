package com.mayantsev_vs.towtracker.userProfile.presentation

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayantsev_vs.towtracker.userProfile.data.UserProfileRepository
import com.mayantsev_vs.towtracker.userProfile.data.UserProfileResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserProfileViewModel(
    private val repository: UserProfileRepository
) : ViewModel() {

    private val _profileState = MutableLiveData<UserProfileUiState>()
    val profileState: LiveData<UserProfileUiState> = _profileState

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _networkError = MutableLiveData<String>()
    val networkError: LiveData<String> = _networkError

    private val _userLiveData: MutableLiveData<UserProfileUiItem> = MutableLiveData()
    val userLiveData: LiveData<UserProfileUiItem> = _userLiveData

    private val _navigationLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val navigationLiveData: LiveData<Boolean> = _navigationLiveData

    val _progressLiveData = MutableLiveData<Int>()
    val progressLiveData: LiveData<Int> = _progressLiveData

    fun changeEdit(isEdit: Boolean) {
        if (isEdit) _profileState.value = UserProfileUiState.Edit
        else _profileState.value = UserProfileUiState.Read
    }

    fun updateUser(email: String, username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(email, username)
        }
    }

    fun updateUserPassword(email: String, password: String, newPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.updateUserPassword(email, password, newPassword)
            if (result is UserProfileResult.Failure) {
                if (result.message != "Нет соединения с интернетом" && result.message != "Ошибка соединения с сервером")
                    _error.postValue(result.message)
                else
                    _networkError.postValue(result.message)
            } else {
                _error.postValue("")
                _networkError.postValue("")
                withContext(Dispatchers.Main) {
                    changeEdit(false)
                }
            }
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
        _networkError.value = ""
        viewModelScope.launch(Dispatchers.IO) {
            val userData = repository.getUser()
            if (userData is UserProfileResult.SuccessUser) {
                _userLiveData.postValue(
                    UserProfileUiItem(
                        userData.login,
                        userData.username
                    )
                )
            } else {
                val failure = userData as UserProfileResult.Failure
                if (failure.message != "Нет соединения с интернетом" && failure.message != "Ошибка соединения с сервером")
                    _error.postValue(failure.message)
                else
                    _networkError.postValue(failure.message)
            }
            _progressLiveData.postValue(View.GONE)
        }
    }

}