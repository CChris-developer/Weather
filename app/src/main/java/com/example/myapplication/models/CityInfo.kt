package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class CityInfo(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("latitude") val latitude: Float,
    @SerializedName("longitude") val longitude: Float,
    @SerializedName("timezone") val timeZone: String,
    @SerializedName("country") val country: String
)
