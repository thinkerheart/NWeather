package com.ngocthanhnguyen.core.data.repository

import com.ngocthanhnguyen.core.common.util.DateTimeProvider
import com.ngocthanhnguyen.core.common.util.defaultIfNull
import com.ngocthanhnguyen.core.database.dao.DbWeatherForecastDao
import com.ngocthanhnguyen.core.database.model.DbWeatherForecast
import com.ngocthanhnguyen.core.domain.entity.Response
import com.ngocthanhnguyen.core.domain.entity.WeatherForecast
import com.ngocthanhnguyen.core.domain.repository.IWeatherRepository
import com.ngocthanhnguyen.core.network.api.WeatherForecastApi
import com.ngocthanhnguyen.core.network.mapper.toWeatherForecast
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.*

class WeatherRepository(
    private val dbWeatherForecastDao: DbWeatherForecastDao,
    private val weatherForecastApi: WeatherForecastApi,
    private val moshi: Moshi,
    private val dateTimeProvider: DateTimeProvider
) : IWeatherRepository {
    companion object {
        const val WEATHER_FORECAST_DATA_EXPIRATION = 5 * 60L * 1000
    }

    override suspend fun getWeatherForecast(
        cityName: String, numberOfForecastDays: Int, units: String
    ): Flow<Response<WeatherForecast>> {
        return flow {
            emit(dbWeatherForecastDao.getDbWeatherForecast(cityName))
        }.catch {
            emit(null)
        }.flatMapConcat {
            if (it == null) {
                fetchWeatherAndPersistIt(cityName, numberOfForecastDays, units)
            } else {
                try {
                    val weatherForecast = moshi.adapter(
                        WeatherForecast::class.java
                    ).fromJson(it.weatherData)
                    if (weatherForecast == null) {
                        fetchWeatherAndPersistIt(cityName, numberOfForecastDays, units)
                    } else {
                        if (isOutdatedWeather(it.updatedAt)) {
                            fetchWeatherAndPersistIt(cityName, numberOfForecastDays, units)
                        } else {
                            flowOf(Response.Success(weatherForecast))
                        }
                    }
                } catch (e: Exception) {
                    fetchWeatherAndPersistIt(cityName, numberOfForecastDays, units)
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun fetchWeatherAndPersistIt(
        cityName: String, numberOfForecastDays: Int, units: String
    ): Flow<Response<WeatherForecast>> {
        return flow {
            emit(
                Response.Success(weatherForecastApi.getWeatherForecast(
                    cityName, numberOfForecastDays, units
                ).toWeatherForecast()) as Response<WeatherForecast>
            )
        }.catch {
            emit(Response.Error(it.localizedMessage.defaultIfNull()))
        }.onEach {
            if (it is Response.Success) {
                dbWeatherForecastDao.insertDbWeatherForecast(
                    DbWeatherForecast(
                        cityId = it.data.city.id.toString(),
                        cityName = cityName,
                        updatedAt = dateTimeProvider.getCurrentTimestamp(),
                        weatherData = moshi.adapter(WeatherForecast::class.java).toJson(it.data)
                    )
                )
            }
        }
    }

    fun isOutdatedWeather(lastWeatherUpdatedTimestamp: Long): Boolean {
        return dateTimeProvider
            .getCurrentTimestamp() - lastWeatherUpdatedTimestamp > WEATHER_FORECAST_DATA_EXPIRATION
    }
}