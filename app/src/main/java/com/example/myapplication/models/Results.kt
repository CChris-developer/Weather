package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class Results (
    @SerializedName("results") val results: List<CityInfo>
)