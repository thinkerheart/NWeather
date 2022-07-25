package com.ngocthanhnguyen.nweather.repository

import com.ngocthanhnguyen.nweather.model.Response
import com.ngocthanhnguyen.nweather.data.disk.dao.APICallResultDao
import com.ngocthanhnguyen.nweather.data.network.NWeatherApiClient.Companion.host
import com.ngocthanhnguyen.nweather.data.network.NWeatherApiClient.Companion.pathSegment
import com.ngocthanhnguyen.nweather.data.network.NWeatherApiClient.Companion.scheme
import com.ngocthanhnguyen.nweather.data.network.api.WeatherForecastApi
import com.ngocthanhnguyen.nweather.model.APICallResult
import com.ngocthanhnguyen.nweather.model.WeatherForecastResponse
import com.ngocthanhnguyen.nweather.util.DateTimeProvider
import com.ngocthanhnguyen.nweather.util.defaultIfNull
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.*
import okhttp3.HttpUrl

class WeatherRepository(
    private val apiCallResultDao: APICallResultDao,
    private val weatherForecastApi: WeatherForecastApi,
    private val moshi: Moshi,
    private val dateTimeProvider: DateTimeProvider
) {
    companion object {
        const val WEATHER_FORECAST_DATA_EXPIRATION = 5 * 60L * 1000
    }

    suspend fun getWeatherForecast(
        cityName: String, numberOfForecastDays: Int, units: String
    ): Flow<Response<WeatherForecastResponse>> {
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
                        WeatherForecastResponse::class.java
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
    ): Flow<Response<WeatherForecastResponse>> {
        return flow {
            emit(
                Response.Success(
                    weatherForecastApi.getWeatherForecast(cityName, numberOfForecastDays, units)
                ) as Response<WeatherForecastResponse>
            )
        }.catch {
            emit(Response.Error(it.localizedMessage.defaultIfNull()))
        }.onEach {
            if (it is Response.Success) {
                apiCallResultDao.insertAPICallResult(
                    APICallResult(
                        url = getWeatherForecastURL(cityName, numberOfForecastDays, units),
                        updatedAt = dateTimeProvider.getCurrentTimestamp(),
                        result = moshi.adapter(WeatherForecastResponse::class.java).toJson(it.data)
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