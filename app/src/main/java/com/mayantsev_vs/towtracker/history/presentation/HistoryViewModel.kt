package com.mayantsev_vs.towtracker.history.presentation

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayantsev_vs.towtracker.history.data.HistoryRepository
import com.mayantsev_vs.towtracker.history.data.HistoryResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val historyRepository: HistoryRepository
): ViewModel() {

    private var _historyLiveData = MutableLiveData<List<HistoryUiItem>>()
    val historyLiveData: LiveData<List<HistoryUiItem>> = _historyLiveData

    private val _error = MutableLiveData<Int>()
    val error: LiveData<Int> = _error

    val _progressLiveData = MutableLiveData<Int>()
    val progressLiveData: LiveData<Int> = _progressLiveData

    fun getHistory() {
        _progressLiveData.value = View.VISIBLE
        viewModelScope.launch(Dispatchers.IO) {
            val result = historyRepository.getHistory()
            if (result is HistoryResult.SuccessHistory) {
                _historyLiveData.postValue(result.history)
                _error.postValue(View.GONE)
            } else {
                _error.postValue(View.VISIBLE)
            }
            _progressLiveData.postValue(View.GONE)
        }
    }
}