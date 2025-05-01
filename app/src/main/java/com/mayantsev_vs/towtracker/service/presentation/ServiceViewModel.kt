package com.mayantsev_vs.towtracker.service.presentation

import androidx.lifecycle.*
import com.mayantsev_vs.towtracker.service.data.cache.ServiceDBO
import com.mayantsev_vs.towtracker.sl.MainDb
import kotlinx.coroutines.launch

class ServiceViewModel(db: MainDb) : ViewModel() {

    private val dao = db.getDaoService()
    val services = dao.getAllServices().asLiveData()

    fun insertService(serviceDBO: ServiceDBO) = viewModelScope.launch {
        dao.insertService(serviceDBO)
    }

    fun deleteService(serviceDBO: ServiceDBO) = viewModelScope.launch {
        dao.deleteService(serviceDBO)
    }

    fun deleteAllServices() = viewModelScope.launch {
        dao.deleteAllServices()
    }
}
