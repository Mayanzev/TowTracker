package com.mayantsev_vs.towtracker.data.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mayantsev_vs.towtracker.data.db.MainDb
import com.mayantsev_vs.towtracker.presentation.MainViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val db: MainDb, private val preferencesHelper: PreferencesHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(db, preferencesHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}