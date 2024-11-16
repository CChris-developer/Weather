package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class Daily(
    @SerializedName("time") val time: List<String>,
    @SerializedName("weather_code") val weatherCode: List<Int>,
    @SerializedName("temperature_2m_max") val temperatureMax: List<Float>,
    @SerializedName("temperature_2m_min") val temperatureMin: List<Float>
)
