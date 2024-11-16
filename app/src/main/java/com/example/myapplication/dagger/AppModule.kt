package com.example.myapplication.dagger

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.myapplication.utils.Consts
import com.example.myapplication.utils.RetrofitGeo
import com.example.myapplication.utils.RetrofitMeteo
import com.example.myapplication.db.WeatherDao
import com.example.myapplication.repository.CitiesListRepository
import com.example.myapplication.repository.CityRepository
import com.example.myapplication.repository.SearchRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module

class AppModule {
    @Provides
    fun provideSearchRepository(retrofit: RetrofitGeo, weatherDao: WeatherDao): SearchRepository = SearchRepository(retrofit, weatherDao)

    @Provides
    fun provideCityRepository(retrofit: RetrofitMeteo, weatherDao: WeatherDao): CityRepository = CityRepository(retrofit, weatherDao)

    @Provides
    fun provideCitiesListRepository(weatherDao: WeatherDao): CitiesListRepository = CitiesListRepository(weatherDao)

    @Singleton
    @Provides
    fun providePreferencesDataStore(context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile(Consts.PREFERENCES_STORE_NAME)
            }
        )
    }
}