package com.mayantsev_vs.towtracker.presentation.services

import androidx.lifecycle.*
import com.mayantsev_vs.towtracker.data.db.MainDb
import com.mayantsev_vs.towtracker.data.db.ServiceItem
import kotlinx.coroutines.launch

class ServiceViewModel(db: MainDb) : ViewModel() {

    private val dao = db.getDao()
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
