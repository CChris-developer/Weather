package com.example.myapplication.dagger

import android.content.Context
import androidx.datastore.core.DataStore
import com.example.myapplication.repository.CitiesListRepository
import com.example.myapplication.repository.CityRepository
import com.example.myapplication.repository.SearchRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component( modules = [ NetModule::class, DataBaseModule::class, AppModule::class ] )
interface DaggerComponent {

   @Component.Factory
   interface Factory {
     fun create(@BindsInstance context: Context): DaggerComponent
   }

   fun getSearchRepository(): SearchRepository
   fun getCityRepository(): CityRepository
   fun getCitiesListRepository(): CitiesListRepository
   fun getPreferencesDataStore(): DataStore<androidx.datastore.preferences.core.Preferences>
}