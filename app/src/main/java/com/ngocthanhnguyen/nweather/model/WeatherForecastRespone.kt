package com.ngocthanhnguyen.nweather.model

import com.squareup.moshi.Json

data class WeatherForecastResponse (
    @Json(name = "city")
    val city: City?,
    @Json(name = "cod")
    val cod: String?,
    @Json(name = "message")
    val message: Double?,
    @Json(name = "cnt")
    val numberOfForecastDays: Int?,
    @Json(name = "list")
    val weatherOfForecastDays: List<DayWeather>?
)

data class City (
    @Json(name = "id")
    val id: Long?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "coord")
    val coord: Coord?,
    @Json(name = "country")
    val country: String?,
    @Json(name = "population")
    val population: Long?,
    @Json(name = "timezone")
    val timezone: Long?
)

data class Coord (
    @Json(name = "lon")
    val lon: Double?,
    @Json(name = "lat")
    val lat: Double?
)

data class DayWeather (
    @Json(name = "dt")
    val dateTime: Long?,
    @Json(name = "sunrise")
    val sunrise: Long?,
    @Json(name = "sunset")
    val sunset: Long?,
    @Json(name = "temp")
    val temp: Temp?,
    @Json(name = "feels_like")
    val feelsLike: FeelsLike?,
    @Json(name = "pressure")
    val pressure: Long?,
    @Json(name = "humidity")
    val humidity: Long?,
    @Json(name = "weather")
    val weather: List<Weather>?,
    @Json(name = "speed")
    val speed: Double?,
    @Json(name = "deg")
    val windDirection: Long?,
    @Json(name = "gust")
    val windGust: Double?,
    @Json(name = "clouds")
    val clouds: Long?,
    @Json(name = "pop")
    val pop: Double?,
    @Json(name = "rain")
    val rain: Double?
)

data class FeelsLike (
    @Json(name = "day")
    val day: Double?,
    @Json(name = "night")
    val night: Double?,
    @Json(name = "eve")
    val eve: Double?,
    @Json(name = "morn")
    val morn: Double?
)

data class Temp (
    @Json(name = "day")
    val day: Double?,
    @Json(name = "min")
    val min: Double?,
    @Json(name = "max")
    val max: Double?,
    @Json(name = "night")
    val night: Double?,
    @Json(name = "eve")
    val eve: Double?,
    @Json(name = "morn")
    val morn: Double?
)

data class Weather (
    @Json(name = "id")
    val id: Long?,
    @Json(name = "main")
    val main: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "icon")
    val icon: String?
)
