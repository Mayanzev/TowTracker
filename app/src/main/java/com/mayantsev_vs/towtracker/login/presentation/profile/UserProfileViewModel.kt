package com.mayantsev_vs.towtracker.login.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayantsev_vs.towtracker.login.data.LoginRepository
import com.mayantsev_vs.towtracker.login.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserProfileViewModel(
    private val repository: LoginRepository
): ViewModel() {

    private val _profileState = MutableLiveData<UserProfileUiState>()
    val profileState: LiveData<UserProfileUiState> = _profileState

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

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
            if (result is Result.Failure) {
                _error.postValue(result.message)
            } else {
                _error.postValue("")
                withContext(Dispatchers.Main) {
                    changeEdit(false)
                }
            }
        }
    }

}