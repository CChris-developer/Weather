package com.example.myapplication.dagger

import com.example.myapplication.utils.Consts.GEO_URL
import com.example.myapplication.utils.Consts.METEO_URL
import com.example.myapplication.utils.RetrofitGeo
import com.example.myapplication.utils.RetrofitMeteo
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class NetModule {

    @Provides
    @Singleton
    fun provideGson(): Gson? {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.serializeNulls().create()
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        })
        return client.build()
    }

    @Provides
    @Singleton
    fun provideRetrofitGeo(gson: Gson?, okHttpClient: OkHttpClient): RetrofitGeo {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .baseUrl(GEO_URL)
            .build()
        return retrofit.create(RetrofitGeo::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofitMeteo(gson: Gson?, okHttpClient: OkHttpClient): RetrofitMeteo {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .baseUrl(METEO_URL)
            .build()
        return retrofit.create(RetrofitMeteo::class.java)
    }
}