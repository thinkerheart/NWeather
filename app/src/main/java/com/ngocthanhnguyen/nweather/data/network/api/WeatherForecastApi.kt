package com.ngocthanhnguyen.nweather.data.network.api

import com.ngocthanhnguyen.nweather.model.WeatherForecastResponse
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
    ): WeatherForecastResponse?
}