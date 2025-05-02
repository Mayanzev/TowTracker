package com.mayantsev_vs.towtracker.history.data.cloud

data class OrderListResponseDTO (
    val orders: List<OrderDTO>
)

data class OrderDTO (
    val date: String,
    val price: String
)

data class OrderRequestDTO(
    val tracks: List<TrackDTO>,
    val services: List<ServiceDTO>
)

data class TrackDTO(
    val id: Int,
    val time: String,
    val date: String,
    val distance: String,
    val speed: String,
    val price: String,
    val firstCity: String,
    val secondCity: String
)

data class ServiceDTO(
    val id: Int,
    val name: String,
    val price: String,
    val date: String
)
