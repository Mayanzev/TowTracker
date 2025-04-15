package com.mayantsev_vs.towtracker.history.presentation

data class HistoryUiItem (
    val tracks: List<TrackUiItem>,
    val services: List<ServiceUiItem>,
    val date: String
)

data class TrackUiItem (
    val time: String,
    val date: String,
    val distance: String,
    val speed: String,
    val price: String,
    val firstCity: String,
    val secondCity: String
)

data class ServiceUiItem (
    val name: String,
    val price: String,
    val date: String
)