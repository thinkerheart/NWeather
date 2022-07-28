package com.ngocthanhnguyen.feature_city_weather_forecast

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ngocthanhnguyen.core.domain.entity.Response
import com.ngocthanhnguyen.feature_city_weather_forecast.util.getOrAwaitValue
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test

class CityWeatherViewModelTest {

    private val cityWeatherViewModel = CityWeatherViewModel(mockk())

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun resetState() {
        cityWeatherViewModel.resetState()
        assertTrue(cityWeatherViewModel.error.getOrAwaitValue() == null)
        assertTrue(cityWeatherViewModel.weatherForecast.getOrAwaitValue() is Response.Uninitialized)
    }
}