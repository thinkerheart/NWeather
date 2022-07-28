package com.ngocthanhnguyen.feature_city_weather_forecast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ngocthanhnguyen.core.domain.entity.Response
import com.ngocthanhnguyen.core.domain.entity.WeatherForecast
import com.ngocthanhnguyen.core.domain.usecase.GetCityWeatherUseCase
import com.ngocthanhnguyen.core.ui.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class CityWeatherViewModel(
    private val getCityWeatherUseCase: GetCityWeatherUseCase
) : BaseViewModel() {
    private var searchJob: Job? = null
    val weatherForecast = MutableLiveData<Response<WeatherForecast>>()
    val searchKeyword = MutableLiveData<String>()

    fun getWeatherForecast(cityName: String, numberOfForecastDays: Int, units: String) {
        searchJob?.cancel()
        resetState()
        searchJob = viewModelScope.launch {
            try {
                weatherForecast.value = Response.Loading()
                getCityWeatherUseCase.execute(
                    GetCityWeatherUseCase.P(cityName, numberOfForecastDays, units)
                ).flowOn(Dispatchers.IO).collect { weatherForecast.value = it }
            } catch (e: Exception) {
                error.value = e.localizedMessage
            }
        }
    }

    fun resetState() {
        weatherForecast.value = Response.Uninitialized()
        error.value = null
    }
}