package com.example.myapplication.repository

import com.example.myapplication.db.WeatherDao
import com.example.myapplication.models.Geo

class CitiesListRepository(private val weatherDao: WeatherDao) {

    fun getAllCities(): List<Geo> {
        return weatherDao.getAllCities()
    }
}