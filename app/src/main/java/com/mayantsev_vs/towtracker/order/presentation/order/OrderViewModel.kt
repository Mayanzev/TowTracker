package com.mayantsev_vs.towtracker.order.presentation.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mayantsev_vs.towtracker.main.utils.PreferencesHelper

class OrderViewModel(private val preferencesHelper: PreferencesHelper) : ViewModel() {

    val isOrderStarted = MutableLiveData<Boolean>()
    val isOrderFinished = MutableLiveData<Boolean>()

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
}
