package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class Geo(
    @SerializedName("name") val name: String,
    @SerializedName("country") val country: String
)
