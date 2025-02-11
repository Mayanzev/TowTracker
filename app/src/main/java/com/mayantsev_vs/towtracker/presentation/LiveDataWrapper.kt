package com.mayantsev_vs.towtracker.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface LiveDataWrapper {
    fun update(value: CurrentScreen)

    fun liveData() : LiveData<CurrentScreen>

    class Base(private val liveData: MutableLiveData<CurrentScreen> = MutableLiveData()) : LiveDataWrapper {
        override fun liveData(): LiveData<CurrentScreen> {
            return liveData
        }

        override fun update(value: CurrentScreen) {
            liveData.value = value
        }
    }
}

enum class CurrentScreen {
    MAP, ORDER
}