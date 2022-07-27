package com.ngocthanhnguyen.core.network.model

data class NetWeatherForecast (
    val city: NetCity?,
    val cod: String?,
    val message: Double?,
    val cnt: Int?,
    val list: List<NetDayWeather>?
)

data class NetCity (
    val id: Long?,
    val name: String?,
    val coord: NetCoord?,
    val country: String?,
    val population: Long?,
    val timezone: Long?
)

data class NetCoord (
    val lon: Double?,
    val lat: Double?
)

data class NetDayWeather (
    val dt: Long?,
    val sunrise: Long?,
    val sunset: Long?,
    val temp: NetTemp?,
    val feels_like: NetFeelsLike?,
    val pressure: Long?,
    val humidity: Long?,
    val weather: List<NetWeather>?,
    val speed: Double?,
    val deg: Long?,
    val gust: Double?,
    val clouds: Long?,
    val pop: Double?,
    val rain: Double?
)

data class NetFeelsLike (
    val day: Double?,
    val night: Double?,
    val eve: Double?,
    val morn: Double?
)

data class NetTemp (
    val day: Double?,
    val min: Double?,
    val max: Double?,
    val night: Double?,
    val eve: Double?,
    val morn: Double?
)

data class NetWeather (
    val id: Long?,
    val main: String?,
    val description: String?,
    val icon: String?
)
