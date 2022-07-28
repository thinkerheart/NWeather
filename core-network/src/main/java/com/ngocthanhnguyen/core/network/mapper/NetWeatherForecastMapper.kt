package com.ngocthanhnguyen.core.network.mapper

import com.ngocthanhnguyen.core.common.util.defaultIfNull
import com.ngocthanhnguyen.core.domain.entity.*
import com.ngocthanhnguyen.core.network.model.*

fun NetWeather?.toWeather(): Weather {
    return Weather(this?.description.defaultIfNull())
}

fun List<NetWeather>?.toWeathers(): List<Weather> {
    return this?.map { it.toWeather() } ?: emptyList()
}

fun NetTemp?.toTemp(): Temp {
    return if (this == null) {
        Temp(0.0)
    } else {
        Temp(this.day.defaultIfNull())
    }
}

fun NetDayWeather?.toDayWeather(): DayWeather {
    return DayWeather(
        this?.dt.defaultIfNull(),
        this?.temp.toTemp(),
        this?.pressure.defaultIfNull(),
        this?.humidity.defaultIfNull(),
        this?.weather.toWeathers()
    )
}

fun List<NetDayWeather>?.toDayWeathers(): List<DayWeather> {
    return this?.map { it.toDayWeather() } ?: emptyList()
}

fun NetCity?.toCity(): City {
    return if (this == null) {
        City(0, 0)
    } else {
        City(this.id.defaultIfNull(), this.timezone.defaultIfNull())
    }
}

fun NetWeatherForecast?.toWeatherForecast(): WeatherForecast {
    return WeatherForecast(
        this?.city.toCity(),
        this?.list.toDayWeathers()
    )
}