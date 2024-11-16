package com.example.myapplication.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class Weather(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "time")
    val time: String,
    @ColumnInfo(name = "weatherCode")
    val weatherCode: Int,
    @ColumnInfo(name = "temperatureMax")
    val temperatureMax: Float,
    @ColumnInfo(name = "temperatureMin")
    val temperatureMin: Float
)