package com.ngocthanhnguyen.core.network.api

import com.ngocthanhnguyen.core.network.model.NetWeatherForecast
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherForecastApi {

    companion object {
        const val dailyForecast = "forecast/daily"
    }

    @GET(dailyForecast)
    suspend fun getWeatherForecast(
        @Query("q") cityName: String,
        @Query("cnt") numberOfForecastDays: Int,
        @Query("units") units: String
    ): NetWeatherForecast?
}