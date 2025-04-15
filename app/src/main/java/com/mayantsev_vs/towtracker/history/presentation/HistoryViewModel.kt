package com.mayantsev_vs.towtracker.history.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayantsev_vs.towtracker.history.data.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val historyRepository: HistoryRepository
): ViewModel() {

    private var _historyLiveData = MutableLiveData<List<HistoryUiItem>>()
    val historyLiveData: LiveData<List<HistoryUiItem>> = _historyLiveData

    fun getHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = historyRepository.getHistory()
            _historyLiveData.postValue(result)
        }
    }
}