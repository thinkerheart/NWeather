package com.ngocthanhnguyen.core.domain.repository

import com.ngocthanhnguyen.core.domain.entity.Response
import com.ngocthanhnguyen.core.domain.entity.WeatherForecast
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {
    suspend fun getWeatherForecast(
        cityName: String, numberOfForecastDays: Int, units: String
    ): Flow<Response<WeatherForecast>>
}