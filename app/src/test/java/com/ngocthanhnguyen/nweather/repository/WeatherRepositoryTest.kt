package com.ngocthanhnguyen.nweather.repository

import com.ngocthanhnguyen.nweather.di.appModule
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.get

class WeatherRepositoryTest: KoinTest {

    private lateinit var weatherRepository: WeatherRepository

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(appModule)
        weatherRepository = WeatherRepository(mockk(), mockk(), mockk(), get())
    }

    @Test
    fun isOutdatedWeather() {
        var millis = System.currentTimeMillis() - WeatherRepository.WEATHER_FORECAST_DATA_EXPIRATION
        assertEquals(weatherRepository.isOutdatedWeather(millis), false)
        millis = System.currentTimeMillis() - WeatherRepository.WEATHER_FORECAST_DATA_EXPIRATION - 100
        assertEquals(weatherRepository.isOutdatedWeather(millis), true)
    }

    @Test
    fun getWeatherForecastURL() {
        assertEquals(
            weatherRepository.getWeatherForecastURL(
                "saigon", 7, "metric"
            ),
            "https://api.openweathermap.org/data%2F2.5?q=saigon&cnt=7&units=metric"
        )
    }
}