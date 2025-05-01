package com.mayantsev_vs.towtracker.history.data.cloud

data class OrderListCloud (
    val orders: List<OrderCloud>
)

data class OrderCloud (
    val tracks: List<TrackCloud>,
    val services: List<ServiceCloud>,
    val date: String
)

data class TrackCloud (
    val time: String,
    val date: String,
    val distance: String,
    val speed: String,
    val price: String,
    val firstCity: String,
    val secondCity: String
)

data class ServiceCloud (
    val name: String,
    val price: String,
    val date: String
)