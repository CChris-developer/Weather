package com.example.myapplication.repository

import com.example.myapplication.utils.RetrofitMeteo
import com.example.myapplication.db.Cities
import com.example.myapplication.db.Weather
import com.example.myapplication.db.WeatherDao
import com.example.myapplication.models.CityInfo
import com.example.myapplication.models.CityWeather
import com.example.myapplication.models.Forecast
import retrofit2.Response

class CityRepository(private val retrofit: RetrofitMeteo, private val weatherDao: WeatherDao) {
    suspend fun getCityWeatherFromNet(
        latitude: Float, longitude: Float, timeZone: String
    ): Response<CityWeather>? {
        return try {
            retrofit.getCityWeather(latitude, longitude, timeZone)
        } catch (e: Exception) {

            null
        }
    }

    fun getCityWeatherFromDb(name: String, country: String): List<Forecast> {
        return weatherDao.getCityWeather(name, country)
    }

    suspend fun insertToWeather(
        name: String,
        country: String,
        time: String,
        weatherCode: Int,
        temperatureMax: Float,
        temperatureMin: Float
    ) {
        weatherDao.insertToWeather(
            Weather(
                id = 0,
                name = name,
                country = country,
                time = time,
                weatherCode = weatherCode,
                temperatureMax = temperatureMax,
                temperatureMin = temperatureMin
            )
        )
    }

    suspend fun insertToCities(
        name: String, country: String, latitude: Float, longitude: Float, timeZone: String
    ) {
        weatherDao.insertToCities(
            Cities(
                id = 0,
                name = name,
                country = country,
                latitude = latitude,
                longitude = longitude,
                timeZone = timeZone
            )
        )
    }

    suspend fun deleteFromWeather(name: String, country: String) {
        weatherDao.deleteFromWeather(name, country)
    }

    suspend fun deleteFromCities(name: String, country: String) {
        weatherDao.deleteFromCities(name, country)
    }

    fun checkCity(name: String, country: String): List<String> {
        return weatherDao.checkCity(name, country)
    }


    fun getCityInfo(name: String, country: String): CityInfo {
        return weatherDao.getCityInfo(name, country)
    }
}