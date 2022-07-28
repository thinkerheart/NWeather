package com.ngocthanhnguyen.feature_city_weather_forecast.mapper

import android.content.Context
import com.ngocthanhnguyen.core.common.util.emptyString
import com.ngocthanhnguyen.core.domain.entity.DayWeather
import com.ngocthanhnguyen.core.domain.entity.WeatherForecast
import com.ngocthanhnguyen.feature_city_weather_forecast.R
import com.ngocthanhnguyen.feature_city_weather_forecast.model.UiDayWeather
import com.ngocthanhnguyen.feature_city_weather_forecast.model.UiWeatherForecast

fun WeatherForecast?.toUiWeatherForecast(context: Context): UiWeatherForecast {
    return if (this == null) {
        UiWeatherForecast(0, 0, emptyList())
    } else {
        if (this.dayWeathers.isEmpty()) {
            UiWeatherForecast(this.city.id, this.city.timezone, emptyList())
        } else {
            UiWeatherForecast(
                this.city.id,
                this.city.timezone,
                this.dayWeathers.toUiDayWeathers(context)
            )
        }
    }
}

fun List<DayWeather>?.toUiDayWeathers(context: Context): List<UiDayWeather> {
    val uiDayWeathers = mutableListOf<UiDayWeather>()
    this?.forEach {
        var description = emptyString()
        it.weather.forEach {
            description += if (description.isEmpty())
                it.description
            else
                "${context.getString(R.string.comma)} ${it.description}"
        }
        uiDayWeathers.add(
            UiDayWeather(it.datetime, it.temp.dayTemp, it.pressure, it.humidity, description)
        )
    }
    return uiDayWeathers
}