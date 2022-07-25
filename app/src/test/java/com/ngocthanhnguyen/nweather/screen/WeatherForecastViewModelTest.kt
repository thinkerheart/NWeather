package com.ngocthanhnguyen.nweather.screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ngocthanhnguyen.nweather.model.Response
import com.ngocthanhnguyen.nweather.util.getOrAwaitValue
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test

class WeatherForecastViewModelTest {

    private val weatherForecastViewModel = WeatherForecastViewModel(mockk())

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun resetState() {
        weatherForecastViewModel.resetState()
        assertEquals(weatherForecastViewModel.error.getOrAwaitValue(), null)
        assertTrue(weatherForecastViewModel.weatherForecast.getOrAwaitValue() is Response.Uninitialized)
    }
}