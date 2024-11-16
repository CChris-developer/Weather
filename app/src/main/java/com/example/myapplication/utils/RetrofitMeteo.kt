package com.example.myapplication.utils

import com.example.myapplication.models.CityWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitMeteo {
    @GET("/v1/forecast?daily=weather_code,temperature_2m_max,temperature_2m_min&forecast_days=1")
    suspend fun getCityWeather(
        @Query("latitude") latitude: Float,
        @Query("longitude") longitude: Float,
        @Query("timezone") timezone: String
    ): Response<CityWeather>
}