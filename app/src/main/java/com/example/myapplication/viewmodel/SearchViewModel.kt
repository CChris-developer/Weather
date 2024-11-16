package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.utils.State
import com.example.myapplication.models.CityInfo
import com.example.myapplication.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: SearchRepository) : ViewModel() {
    var cityFromNet = listOf<CityInfo>()
    var latitude = 0.0f
    private val _state = MutableStateFlow<State>(State.Success)
    val state = _state.asStateFlow()


    fun getCity(name: String): List<CityInfo> {
        var cityFromDb = listOf<CityInfo>()
        var isReady = false
        viewModelScope.launch(Dispatchers.IO) {
            cityFromDb = repository.getCityFromDb(name)
            if (cityFromDb.isEmpty()) {
                cityFromNet = getCityFromNet(name)
            }
            isReady = true
        }
        while (!isReady) {
            viewModelScope.launch(Dispatchers.IO) {
                delay(300)
            }
        }
        return cityFromDb
    }

    private fun getCityFromNet(name: String): List<CityInfo> {
        var isReady = false
        var result = listOf<CityInfo>()
        viewModelScope.launch(Dispatchers.IO) {
            val request = repository.getCityFromNet(name)
            if (request != null) {
                if (request.body()?.results != null) {
                    _state.value = State.Success
                    result = request.body()?.results!!
                } else {
                    _state.value = State.NoData("Город не найден")
                }
            } else {
                _state.value = State.Error("Проверьте подключение к Интернет")
            }
            isReady = true
        }
        while (!isReady) {
            viewModelScope.launch(Dispatchers.IO) {
                delay(300)
            }
        }
        return result
    }
}