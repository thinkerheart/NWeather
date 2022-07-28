package com.ngocthanhnguyen.feature_city_weather_forecast.model

class UiWeatherForecast (
    val cityId: Long,
    val cityTimezone: Long,
    val dayWeathers: List<UiDayWeather>
)

data class UiDayWeather (
    val datetime: Long,
    val dayTemp: Double,
    val pressure: Long,
    val humidity: Long,
    val description: String
)