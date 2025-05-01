package com.mayantsev_vs.towtracker.history.data.cloud

data class OrderListResponseDTO (
    val orders: List<OrderDTO>
)

data class OrderDTO (
    val tracks: List<TrackDTO>,
    val services: List<ServiceDTO>,
    val date: String
)

data class TrackDTO (
    val time: String,
    val date: String,
    val distance: String,
    val speed: String,
    val price: String,
    val firstCity: String,
    val secondCity: String
)

data class ServiceDTO (
    val name: String,
    val price: String,
    val date: String
)