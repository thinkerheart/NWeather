package com.ngocthanhnguyen.core.domain.entity

data class WeatherForecast (
    val city: City,
    val dayWeathers: List<DayWeather>
)

data class City (
    val id: Long,
    val timezone: Long
)

data class DayWeather (
    val datetime: Long,
    val temp: Temp,
    val pressure: Long,
    val humidity: Long,
    val weather: List<Weather>
)

data class Temp (
    val dayTemp: Double,
)

data class Weather (
    val description: String
)