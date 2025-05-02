package com.mayantsev_vs.towtracker.order.presentation

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayantsev_vs.towtracker.history.data.HistoryRepository
import com.mayantsev_vs.towtracker.main.utils.PreferencesHelper
import com.mayantsev_vs.towtracker.main.utils.generateAndSavePdf
import com.mayantsev_vs.towtracker.service.data.cache.ServiceDao
import com.mayantsev_vs.towtracker.track.data.cache.TrackDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderViewModel(
    private val preferencesHelper: PreferencesHelper,
    private val serviceDao: ServiceDao,
    private val trackDao: TrackDao,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    val isOrderStarted = MutableLiveData<Boolean>()
    val isOrderFinished = MutableLiveData<Boolean>()

    val _savePdfLiveData = MutableLiveData<Boolean>()
    val savePdfLiveData: LiveData<Boolean> = _savePdfLiveData

    init {
        loadOrderState()
    }

    private fun loadOrderState() {
        isOrderStarted.value = preferencesHelper.isOrderStarted
    }

    private fun saveOrderState(isStarted: Boolean) {
        preferencesHelper.isOrderStarted = isStarted
    }

    fun activeOrder() {
        isOrderStarted.value = true
        saveOrderState(true)
    }

    fun emptyOrder() {
        isOrderStarted.value = false
        saveOrderState(false)
    }

    fun updateFinishOrder(value: Boolean) {
        isOrderFinished.value = value
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun savePdf(value: Boolean, context: Context, fileName: String) {
        _savePdfLiveData.value = value
        generateAndSavePdf(context, fileName, serviceDao, trackDao, viewModelScope)
    }

    fun postHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.postHistory()
            serviceDao.deleteAllServices()
            trackDao.deleteAllTracks()
        }
    }
}
