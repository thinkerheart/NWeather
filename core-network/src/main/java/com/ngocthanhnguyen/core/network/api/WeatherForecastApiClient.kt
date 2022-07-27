package com.ngocthanhnguyen.core.network.api

import com.ngocthanhnguyen.core.common.util.defaultIfNull
import com.ngocthanhnguyen.core.domain.entity.*
import com.ngocthanhnguyen.core.network.model.*

class WeatherForecastApiClient(private val weatherForecastApi: WeatherForecastApi) {

    suspend fun getWeatherForecast(
        cityName: String, numberOfForecastDays: Int, units: String
    ) : WeatherForecast {
        return weatherForecastApi.getWeatherForecast(
            cityName, numberOfForecastDays, units
        ).toWeatherForecast()
    }

    private fun NetWeather?.toWeather(): Weather {
        return Weather(this?.description.defaultIfNull())
    }

    private fun List<NetWeather>?.toWeathers(): List<Weather> {
        return this?.map { it.toWeather() } ?: emptyList()
    }

    private fun NetTemp?.toTemp(): Temp {
        return if (this == null) {
            Temp(0.0)
        } else {
            Temp(this.day.defaultIfNull())
        }
    }

    private fun NetDayWeather?.toDayWeather(): DayWeather {
        return DayWeather(
            this?.dt.defaultIfNull(),
            this?.temp.toTemp(),
            this?.pressure.defaultIfNull(),
            this?.humidity.defaultIfNull(),
            this?.weather.toWeathers()
        )
    }

    private fun List<NetDayWeather>?.toDayWeathers(): List<DayWeather> {
        return this?.map { it.toDayWeather() } ?: emptyList()
    }

    private fun NetCity?.toCity(): City {
        return if (this == null) {
            City(0, 0)
        } else {
            City(this.id.defaultIfNull(), this.timezone.defaultIfNull())
        }
    }

    private fun NetWeatherForecast?.toWeatherForecast(): WeatherForecast {
        return WeatherForecast(
            this?.city.toCity(),
            this?.list.toDayWeathers()
        )
    }
}