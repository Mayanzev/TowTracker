package com.mayantsev_vs.towtracker.order.presentation.services

import androidx.lifecycle.*
import com.mayantsev_vs.towtracker.order.data.cache.ServiceItem
import com.mayantsev_vs.towtracker.sl.MainDb
import kotlinx.coroutines.launch

class ServiceViewModel(db: MainDb) : ViewModel() {

    private val dao = db.getDaoService()
    val services = dao.getAllServices().asLiveData()

    fun insertService(serviceItem: ServiceItem) = viewModelScope.launch {
        dao.insertService(serviceItem)
    }

    fun deleteService(serviceItem: ServiceItem) = viewModelScope.launch {
        dao.deleteService(serviceItem)
    }

    fun deleteAllServices() = viewModelScope.launch {
        dao.deleteAllServices()
    }
}
