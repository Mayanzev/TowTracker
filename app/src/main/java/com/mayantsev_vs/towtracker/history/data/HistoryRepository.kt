package com.mayantsev_vs.towtracker.history.data

import com.mayantsev_vs.towtracker.auth.data.cache.AuthDao
import com.mayantsev_vs.towtracker.history.data.cloud.HistoryService
import com.mayantsev_vs.towtracker.history.data.cloud.OrderRequestDTO
import com.mayantsev_vs.towtracker.history.data.cloud.ServiceDTO
import com.mayantsev_vs.towtracker.history.data.cloud.TrackDTO
import com.mayantsev_vs.towtracker.history.presentation.HistoryUiItem
import com.mayantsev_vs.towtracker.main.utils.TimeUtils
import com.mayantsev_vs.towtracker.service.data.cache.ServiceDao
import com.mayantsev_vs.towtracker.track.data.cache.TrackDao
import retrofit2.HttpException
import java.net.ConnectException

class HistoryRepository(
    private val historyService: HistoryService,
    private val authDao: AuthDao,
    private val trackDao: TrackDao,
    private val serviceDao: ServiceDao
) {
    suspend fun getHistory(): HistoryResult {
        try {
            val result = historyService.getHistory(authDao.getToken() ?: "").orders.map { order ->
                val date = TimeUtils.formatIsoDateToReadable(order.date)
                HistoryUiItem(
                    date = date,
                    price = order.price
                )
            }
            return HistoryResult.SuccessHistory(result)
        } catch (_: ConnectException) {
            return HistoryResult.Failure("Нет соединения с интернетом")
        } catch (e: HttpException) {
            return HistoryResult.Failure(e.response()?.errorBody()?.string() ?: "")
        } catch (_: Exception) {
            return HistoryResult.Failure("Ошибка соединения с сервером")
        }
    }

    suspend fun postHistory(): HistoryResult {
        try {
            val tracksDTO = trackDao.getAllTracksList().map { track ->
                TrackDTO(
                    id = track.id ?: -1,
                    time = track.time,
                    date = track.date,
                    distance = track.distance,
                    speed = track.speed,
                    price = track.price,
                    firstCity = track.firstCity ?: "",
                    secondCity = track.secondCity ?: ""
                )
            }
            val serviceDTO = serviceDao.getAllServicesList().map { service ->
                ServiceDTO(
                    id = service.id ?: -1,
                    name = service.name,
                    price = service.price,
                    date = service.date
                )
            }
            val orderRequestDTO = OrderRequestDTO(tracksDTO, serviceDTO)
            historyService.postHistory(authDao.getToken() ?: "", orderRequestDTO)
            return HistoryResult.Success
        } catch (_: ConnectException) {
            return HistoryResult.Failure("Нет соединения с интернетом")
        } catch (e: HttpException) {
            return HistoryResult.Failure(e.response()?.errorBody()?.string() ?: "")
        } catch (_: Exception) {
            return HistoryResult.Failure("Ошибка соединения с сервером")
        }
    }
}