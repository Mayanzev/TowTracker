package com.mayantsev_vs.towtracker.data.cloud

import com.google.gson.annotations.SerializedName

data class GeocodeResponse(
    val address: Address
)

data class Address(
    val city: String?,
    val town: String?,
    val village: String?,
    val road: String?,
    val suburb: String?,
    val country: String?,
    val state: String?,
    val postcode: String?,
    @SerializedName("house_number")
    val houseNumber: String?
)