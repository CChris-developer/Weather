package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.utils.State
import com.example.myapplication.utils.Utils
import com.example.myapplication.models.CityInfo
import com.example.myapplication.models.Forecast
import com.example.myapplication.repository.CityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CityViewModel(private val repository: CityRepository) : ViewModel() {
    private var forecast: Forecast? = null
    var isDeleted = false
    private var cityInfo: CityInfo? = null
    private val _state = MutableStateFlow<State>(State.Success)
    val state = _state.asStateFlow()

    fun getCityWeatherFromDb(name: String, country: String): List<Forecast> {
        var isReady = false
        var forecastList = listOf<Forecast>()
        viewModelScope.launch(Dispatchers.IO) {
            forecastList = repository.getCityWeatherFromDb(name, country)
            isReady = true
        }
        while (!isReady) {
            viewModelScope.launch(Dispatchers.IO) {
                delay(300)
            }
        }
        return forecastList
    }

    fun updateWeather(
        name: String,
        country: String,
        latitude: Float,
        longitude: Float,
        timeZone: String
    ): List<Forecast> {
        var isReady = false
        var forecastList = listOf<Forecast>()
        viewModelScope.launch(Dispatchers.IO) {
            val request = repository.getCityWeatherFromNet(latitude, longitude, timeZone)
            if (request != null) {
                if (request.body()?.daily != null) {
                    _state.value = State.Success
                    val dailyForecast = request.body()?.daily
                    val forecastTime = Utils.formatDateView(dailyForecast!!.time.first())
                    repository.insertToWeather(
                        name,
                        country,
                        forecastTime,
                        dailyForecast.weatherCode.first(),
                        dailyForecast.temperatureMax.first(),
                        dailyForecast.temperatureMin.first()
                    )
                    forecastList = repository.getCityWeatherFromDb(name, country)
                } else {
                    forecastList = repository.getCityWeatherFromDb(name, country)
                    _state.value =
                        State.NoData("Не удалось обновить данные. Данные о погоде не найдены")
                }
            } else {
                forecastList = repository.getCityWeatherFromDb(name, country)
                _state.value =
                    State.Error("Не удалось обновить данные. Проверьте подключение к Интернет")
            }
            isReady = true
        }
        while (!isReady) {
            viewModelScope.launch(Dispatchers.IO) {
                delay(300)
            }
        }
        return forecastList

    }

    fun getCityWeatherFromNet(
        name: String,
        country: String,
        latitude: Float,
        longitude: Float,
        timeZone: String
    ): Forecast? {
        var isReady = false
        viewModelScope.launch(Dispatchers.IO) {
            val request = repository.getCityWeatherFromNet(latitude, longitude, timeZone)
            if (request != null) {
                if (request.body()?.daily != null) {
                    _state.value = State.Success
                    val dailyForecast = request.body()?.daily
                    val forecastTime = Utils.formatDateView(dailyForecast!!.time.first())
                    if (repository.checkCity(name, country).isEmpty())
                        repository.insertToCities(name, country, latitude, longitude, timeZone)
                    repository.insertToWeather(
                        name,
                        country,
                        forecastTime,
                        dailyForecast.weatherCode.first(),
                        dailyForecast.temperatureMax.first(),
                        dailyForecast.temperatureMin.first()
                    )
                    forecast = Forecast(
                        forecastTime,
                        dailyForecast.weatherCode.first(),
                        dailyForecast.temperatureMax.first(),
                        dailyForecast.temperatureMin.first()
                    )
                } else {
                    _state.value = State.NoData("Данные о погоде не найдены")
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
        return forecast
    }

    fun getCityInfo(name: String, country: String): CityInfo? {
        var isReady = false
        viewModelScope.launch(Dispatchers.IO) {
            cityInfo = repository.getCityInfo(name, country)
            isReady = true
        }
        while (!isReady) {
            viewModelScope.launch(Dispatchers.IO) {
                delay(300)
            }
        }
        return cityInfo
    }

    fun deleteCity(name: String, country: String) {
        var isReady = false
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFromWeather(name, country)
            repository.deleteFromCities(name, country)
            isReady = true
        }
        while (!isReady) {
            viewModelScope.launch(Dispatchers.IO) {
                delay(300)
            }
        }
        isDeleted = true
    }
}