package com.mayantsev_vs.towtracker.history.data

import com.mayantsev_vs.towtracker.history.data.cloud.HistoryService
import com.mayantsev_vs.towtracker.history.presentation.HistoryUiItem
import com.mayantsev_vs.towtracker.history.presentation.ServiceUiItem
import com.mayantsev_vs.towtracker.history.presentation.TrackUiItem
import com.mayantsev_vs.towtracker.auth.data.cache.AuthDao

class HistoryRepository(
    private val historyService: HistoryService,
    private val userDao: AuthDao
) {
    suspend fun getHistory(): List<HistoryUiItem> {
        val result = historyService.getHistory(userDao.getToken() ?: "").orders.map { order ->
            HistoryUiItem(
                tracks = order.tracks.map {
                    TrackUiItem(
                        it.time,
                        it.date,
                        it.distance,
                        it.speed,
                        it.price,
                        it.firstCity,
                        it.secondCity
                    )
                },
                services = order.services.map {
                    ServiceUiItem(
                        it.name,
                        it.price,
                        it.date
                    )
                },
                date = order.date
            )
        }
        return result
    }
}