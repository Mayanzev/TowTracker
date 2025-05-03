package com.mayantsev_vs.towtracker.history.data

import com.mayantsev_vs.towtracker.auth.data.AuthResult
import com.mayantsev_vs.towtracker.history.presentation.HistoryUiItem

sealed class HistoryResult {
    data object Success : HistoryResult()
    data class Failure(val message: String) : HistoryResult()
    data class SuccessHistory(
        val history: List<HistoryUiItem>
    ) : HistoryResult()
}