package com.mayantsev_vs.towtracker.track.data.cloud

import com.google.gson.annotations.SerializedName

data class GeocodeResponseDTO(
    val geocodeAddressDTO: GeocodeAddressDTO
)

data class GeocodeAddressDTO(
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