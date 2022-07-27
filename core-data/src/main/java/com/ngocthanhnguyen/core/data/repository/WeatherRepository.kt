package com.ngocthanhnguyen.core.data.repository

import com.ngocthanhnguyen.core.common.util.DateTimeProvider
import com.ngocthanhnguyen.core.common.util.defaultIfNull
import com.ngocthanhnguyen.core.database.dao.APICallResultDao
import com.ngocthanhnguyen.core.database.model.APICallResult
import com.ngocthanhnguyen.core.domain.entity.Response
import com.ngocthanhnguyen.core.domain.entity.WeatherForecast
import com.ngocthanhnguyen.core.domain.repository.IWeatherRepository
import com.ngocthanhnguyen.core.network.NWeatherApiClient.Companion.host
import com.ngocthanhnguyen.core.network.NWeatherApiClient.Companion.pathSegment
import com.ngocthanhnguyen.core.network.NWeatherApiClient.Companion.scheme
import com.ngocthanhnguyen.core.network.api.WeatherForecastApiClient
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.*
import okhttp3.HttpUrl

class WeatherRepository(
    private val apiCallResultDao: APICallResultDao,
    private val weatherForecastApiClient: WeatherForecastApiClient,
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
            emit(
                apiCallResultDao.getAPICallResult(
                    getWeatherForecastURL(cityName, numberOfForecastDays, units)
                )
            )
        }.catch {
            emit(null)
        }.flatMapConcat {
            if (it == null) {
                fetchWeatherAndPersistIt(cityName, numberOfForecastDays, units)
            } else {
                try {
                    val weatherForecastResponse = moshi.adapter(
                        WeatherForecast::class.java
                    ).fromJson(it.result)
                    if (isOutdatedWeather(it.updatedAt)) {
                        fetchWeatherAndPersistIt(cityName, numberOfForecastDays, units)
                    } else {
                        flowOf(Response.Success(weatherForecastResponse!!))
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
                Response.Success(weatherForecastApiClient.getWeatherForecast(
                    cityName, numberOfForecastDays, units
                )) as Response<WeatherForecast>
            )
        }.catch {
            emit(Response.Error(it.localizedMessage.defaultIfNull()))
        }.onEach {
            if (it is Response.Success) {
                apiCallResultDao.insertAPICallResult(
                    APICallResult(
                        url = getWeatherForecastURL(cityName, numberOfForecastDays, units),
                        updatedAt = dateTimeProvider.getCurrentTimestamp(),
                        result = moshi.adapter(WeatherForecast::class.java).toJson(it.data)
                    )
                )
            }
        }
    }

    fun isOutdatedWeather(lastWeatherUpdatedTimestamp: Long): Boolean {
        return dateTimeProvider
            .getCurrentTimestamp() - lastWeatherUpdatedTimestamp > WEATHER_FORECAST_DATA_EXPIRATION
    }

    fun getWeatherForecastURL(
        cityName: String, numberOfForecastDays: Int, units: String
    ): String {
        return HttpUrl.Builder()
            .scheme(scheme)
            .host(host)
            .addPathSegment(pathSegment)
            .addQueryParameter("q", cityName)
            .addQueryParameter("cnt", numberOfForecastDays.toString())
            .addQueryParameter("units", units)
            .build()
            .toString()
    }
}