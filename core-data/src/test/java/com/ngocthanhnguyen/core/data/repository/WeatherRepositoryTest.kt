package com.ngocthanhnguyen.core.data.repository

import com.ngocthanhnguyen.core.common.di.coreCommonModule
import com.ngocthanhnguyen.core.data.di.coreDataModule
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.get

class WeatherRepositoryTest : KoinTest {

    private lateinit var weatherRepository: WeatherRepository

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(coreCommonModule)
        modules(coreDataModule)
        weatherRepository = WeatherRepository(
            mockk(),
            mockk(),
            mockk(),
            get()
        )
    }

    @Test
    fun isOutdatedWeather() {
        var millis = System.currentTimeMillis() - WeatherRepository.WEATHER_FORECAST_DATA_EXPIRATION
        assertEquals(weatherRepository.isOutdatedWeather(millis), false)
        millis = System.currentTimeMillis() - WeatherRepository.WEATHER_FORECAST_DATA_EXPIRATION - 100
        assertEquals(weatherRepository.isOutdatedWeather(millis), true)
    }
}