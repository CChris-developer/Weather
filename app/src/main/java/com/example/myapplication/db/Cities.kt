package com.example.myapplication.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class Cities(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "latitude")
    val latitude: Float,
    @ColumnInfo(name = "longitude")
    val longitude: Float,
    @ColumnInfo(name = "timeZone")
    val timeZone: String
)
