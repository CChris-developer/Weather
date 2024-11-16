package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class CityWeather(
    @SerializedName("latitude") val latitude: Float,
    @SerializedName("longitude") val longitude: Float,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("timezone_abbreviation") val timezoneAbbreviation: String,
    @SerializedName("daily") val daily: Daily
)