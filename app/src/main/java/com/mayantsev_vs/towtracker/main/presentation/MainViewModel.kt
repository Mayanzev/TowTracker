package com.mayantsev_vs.towtracker.main.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mayantsev_vs.towtracker.main.presentation.MainFragment.CurrentScreen

class MainViewModel : ViewModel() {
    private val _bottomNavigation = MutableLiveData<CurrentScreen>()
    val bottomNavigation: LiveData<CurrentScreen> = _bottomNavigation

    fun changeBottomNavigation(currentScreen: CurrentScreen) {
        _bottomNavigation.value = currentScreen
    }
}