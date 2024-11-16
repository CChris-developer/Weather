package com.example.myapplication.utils

import com.example.myapplication.models.Results
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitGeo {

    @GET("/v1/search?count=10&language=ru&format=json")
    suspend fun getCity(@Query("name") name: String): Response<Results>
}