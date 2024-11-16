package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.models.Geo
import com.example.myapplication.repository.CitiesListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CitiesListViewModel(private val repository: CitiesListRepository) : ViewModel() {

    fun getAllCities(): List<Geo> {
        var citiesList = listOf<Geo>()
        var isReady = false
        viewModelScope.launch(Dispatchers.IO) {
            citiesList = repository.getAllCities()
            isReady = true
        }
        while (!isReady) {
            viewModelScope.launch(Dispatchers.IO) {
                delay(300)
            }
        }
        return citiesList
    }

}