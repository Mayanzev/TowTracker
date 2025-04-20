package com.mayantsev_vs.towtracker.sl

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mayantsev_vs.towtracker.history.data.HistoryRepository
import com.mayantsev_vs.towtracker.history.data.cloud.HistoryService
import com.mayantsev_vs.towtracker.history.presentation.HistoryViewModel
import com.mayantsev_vs.towtracker.order.data.cloud.NominatimService
import com.mayantsev_vs.towtracker.main.utils.PreferencesHelper
import com.mayantsev_vs.towtracker.login.data.LoginRepository
import com.mayantsev_vs.towtracker.login.data.cloud.LoginService
import com.mayantsev_vs.towtracker.login.presentation.login.LoginViewModel
import com.mayantsev_vs.towtracker.userProfile.presentation.UserProfileViewModel
import com.mayantsev_vs.towtracker.main.presentation.MainViewModel
import com.mayantsev_vs.towtracker.map.presentation.MapViewModel
import com.mayantsev_vs.towtracker.order.presentation.OrderViewModel
import com.mayantsev_vs.towtracker.service.presentation.ServiceViewModel
import com.mayantsev_vs.towtracker.track.presentation.TrackViewModel
import com.mayantsev_vs.towtracker.userProfile.data.UserProfileRepository
import com.mayantsev_vs.towtracker.userProfile.data.cloud.UserProfileService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val database by lazy { MainDb.getDatabase(context) }
    private val preferencesHelper = PreferencesHelper(context)

    private val baseUrl = "http://10.0.2.2:8080/"
    private val baseUrlState = "https://e60c-94-142-136-113.ngrok-free.app/"

    private val loginService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(LoginService::class.java)
    private val repository = LoginRepository(loginService, database.getDaoUser())

    val retrofit = Retrofit.Builder()
        .baseUrl("https://nominatim.openstreetmap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val nominatimService = retrofit.create(NominatimService::class.java)

    val historyService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(HistoryService::class.java)
    val historyRepository = HistoryRepository(historyService, database.getDaoUser())

    private val userProfileService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserProfileService::class.java)
    private val userProfileRepository = UserProfileRepository(userProfileService, database.getDaoUser())

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TrackViewModel::class.java) -> TrackViewModel(
                database,
                nominatimService
            ) as T

            modelClass.isAssignableFrom(ServiceViewModel::class.java) -> ServiceViewModel(database) as T
            modelClass.isAssignableFrom(MapViewModel::class.java) -> MapViewModel() as T
            modelClass.isAssignableFrom(OrderViewModel::class.java) -> OrderViewModel(
                preferencesHelper,
                database.getDaoUser(),
                database.getDaoService(),
                database.getDaoTrack()
            ) as T

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repository) as T
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel() as T
            modelClass.isAssignableFrom(UserProfileViewModel::class.java) -> UserProfileViewModel(userProfileRepository) as T
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> HistoryViewModel(historyRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}