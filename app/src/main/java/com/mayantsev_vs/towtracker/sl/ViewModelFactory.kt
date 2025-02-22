package com.mayantsev_vs.towtracker.sl

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mayantsev_vs.towtracker.data.utils.PreferencesHelper
import com.mayantsev_vs.towtracker.presentation.map.MapViewModel
import com.mayantsev_vs.towtracker.presentation.order.OrderViewModel
import com.mayantsev_vs.towtracker.presentation.services.ServiceViewModel
import com.mayantsev_vs.towtracker.presentation.tracks.TrackViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(context: Context) : ViewModelProvider.Factory {

    val database = (context as MainApp).database
    val preferencesHelper = PreferencesHelper(context)


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TrackViewModel::class.java) -> TrackViewModel(database) as T
            modelClass.isAssignableFrom(ServiceViewModel::class.java) -> ServiceViewModel(database) as T
            modelClass.isAssignableFrom(MapViewModel::class.java) -> MapViewModel() as T
            modelClass.isAssignableFrom(OrderViewModel::class.java) -> OrderViewModel(preferencesHelper) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}