package com.mayantsev_vs.towtracker.sl

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mayantsev_vs.towtracker.order.data.cloud.NominatimService
import com.mayantsev_vs.towtracker.main.utils.PreferencesHelper
import com.mayantsev_vs.towtracker.login.data.LoginRepository
import com.mayantsev_vs.towtracker.login.data.cloud.LoginService
import com.mayantsev_vs.towtracker.login.presentation.LoginViewModel
import com.mayantsev_vs.towtracker.login.presentation.ProfileViewModel
import com.mayantsev_vs.towtracker.main.presentation.MainViewModel
import com.mayantsev_vs.towtracker.map.presentation.MapViewModel
import com.mayantsev_vs.towtracker.order.presentation.order.OrderViewModel
import com.mayantsev_vs.towtracker.order.presentation.services.ServiceViewModel
import com.mayantsev_vs.towtracker.order.presentation.tracks.TrackViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val database by lazy { MainDb.getDatabase(context) }
    private val preferencesHelper = PreferencesHelper(context)

    private val loginService = Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").addConverterFactory(
        GsonConverterFactory.create()).build().create(LoginService::class.java)
    private val repository = LoginRepository(loginService, database.getDaoUser())

    val retrofit = Retrofit.Builder()
        .baseUrl("https://nominatim.openstreetmap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val nominatimService = retrofit.create(NominatimService::class.java)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TrackViewModel::class.java) -> TrackViewModel(database, nominatimService) as T
            modelClass.isAssignableFrom(ServiceViewModel::class.java) -> ServiceViewModel(database) as T
            modelClass.isAssignableFrom(MapViewModel::class.java) -> MapViewModel() as T
            modelClass.isAssignableFrom(OrderViewModel::class.java) -> OrderViewModel(preferencesHelper) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repository) as T
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel() as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}