package com.example.myapplication.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.models.CityInfo
import com.example.myapplication.models.Forecast
import com.example.myapplication.models.Geo

@Dao
interface WeatherDao {
    @Query("SELECT * FROM cities where name like :name || '%'")
    fun getCity(name: String): List<CityInfo>

    @Query("SELECT time, weatherCode, temperatureMax, temperatureMin FROM weather where name = :name and country = :country")
    fun getCityWeather(name: String, country: String): List<Forecast>

    @Query("SELECT * FROM weather where name = :name and country = :country and time = :time")
    fun checkTime(name: String, country: String, time: String): Weather

    @Query("SELECT name FROM cities where name = :name and country = :country")
    fun checkCity(name: String, country: String): List<String>

    @Query("SELECT * FROM cities where name = :name and country = :country")
    fun getCityInfo(name: String, country: String): CityInfo

    @Query("SELECT DISTINCT name, country FROM weather ORDER BY name")
    fun getAllCities(): List<Geo>

    @Query("DELETE FROM weather where name = :name and country = :country")
    suspend fun deleteFromWeather(name: String, country: String)

    @Query("DELETE FROM cities where name = :name and country = :country")
    suspend fun deleteFromCities(name: String, country: String)

    @Insert(entity = Cities::class)
    suspend fun insertToCities(city: Cities)

    @Insert(entity = Weather::class)
    suspend fun insertToWeather(weather: Weather)

    @Query("UPDATE weather SET temperatureMax = :tempMax, weatherCode = :weatherCode WHERE name = :name and country = :country")
    suspend fun updateWeather(tempMax: Float, weatherCode: Int, name: String, country: String)
}