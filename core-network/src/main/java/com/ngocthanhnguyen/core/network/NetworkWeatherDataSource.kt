package com.ngocthanhnguyen.core.network

import com.ngocthanhnguyen.core.network.model.NetWeatherForecast

interface NetworkWeatherDataSource {
    suspend fun getWeatherForecast(
        cityName: String,
        numberOfForecastDays: Int,
        units: String
    ): NetWeatherForecast?
}