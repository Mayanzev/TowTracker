package com.mayantsev_vs.towtracker.data.cloud

data class GeocodeResponse(
    val address: Address
)

data class Address(
    val city: String?,
    val town: String?,
    val village: String?
)