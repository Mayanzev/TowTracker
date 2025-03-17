package com.mayantsev_vs.towtracker.login.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayantsev_vs.towtracker.login.data.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: LoginRepository
): ViewModel() {

    private val _profileState = MutableLiveData<ProfileUiState>()
    val profileState: LiveData<ProfileUiState> = _profileState

    fun changeEdit(isEdit: Boolean) {
        if (isEdit) _profileState.value = ProfileUiState.Edit
        else _profileState.value = ProfileUiState.Read
    }

    fun updateUser(email: String, username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(email, username, password)
        }
    }

}