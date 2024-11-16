package com.example.myapplication.repository

import com.example.myapplication.utils.RetrofitGeo
import com.example.myapplication.db.WeatherDao
import com.example.myapplication.models.CityInfo
import com.example.myapplication.models.Results
import retrofit2.Response
import java.lang.Exception

class SearchRepository(private val retrofit: RetrofitGeo, private val weatherDao: WeatherDao) {
    suspend fun getCityFromNet(name: String): Response<Results>? {
         return try {
            retrofit.getCity(name)
         } catch (e: Exception) {
           null
        }
    }

    fun getCityFromDb(name: String): List<CityInfo> {
        return weatherDao.getCity(name)
    }
}