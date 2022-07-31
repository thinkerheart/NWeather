package com.ngocthanhnguyen.core.data.repository

import com.ngocthanhnguyen.core.common.util.DateTimeProvider
import com.ngocthanhnguyen.core.common.util.emptyString
import com.ngocthanhnguyen.core.database.dao.DbWeatherForecastDao
import com.ngocthanhnguyen.core.database.model.DbWeatherForecast
import com.ngocthanhnguyen.core.domain.entity.City
import com.ngocthanhnguyen.core.domain.entity.Response
import com.ngocthanhnguyen.core.domain.entity.WeatherForecast
import com.ngocthanhnguyen.core.network.api.WeatherForecastApi
import com.ngocthanhnguyen.core.network.fake.FakeNetworkSource
import com.squareup.moshi.Moshi
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Test

class WeatherRepositoryTest {

    private val dbWeatherForecastDao: DbWeatherForecastDao = mockk()
    private val weatherForecastApi: WeatherForecastApi = mockk()
    private val moshi: Moshi = mockk()
    private val dateTimeProvider: DateTimeProvider = mockk()
    private val weatherRepository = WeatherRepository(
        dbWeatherForecastDao, weatherForecastApi, moshi, dateTimeProvider
    )
    private val spyWeatherRepository = spyk(weatherRepository)

    @Test
    fun isOutdatedWeather() {
        val currentTimeMillis = System.currentTimeMillis()
        every { dateTimeProvider.getCurrentTimestamp() } returns currentTimeMillis
        var millis = currentTimeMillis - WeatherRepository.WEATHER_FORECAST_DATA_EXPIRATION
        assertEquals(weatherRepository.isOutdatedWeather(millis), false)
        millis = currentTimeMillis - WeatherRepository.WEATHER_FORECAST_DATA_EXPIRATION - 100
        assertEquals(weatherRepository.isOutdatedWeather(millis), true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun fetchWeatherAndPersistIt_weatherForecastApiThrowsError_returnsResponseError() {
        runTest {
            coEvery {
                weatherForecastApi.getWeatherForecast(
                    emptyString(),
                    0, emptyString()
                )
            }.throws(Exception("Fake error!"))

            val item = weatherRepository.fetchWeatherAndPersistIt(
                emptyString(),
                0,
                emptyString()
            ).first()

            assertTrue(item is Response.Error)
            assertTrue((item as Response.Error).errorValue == "Fake error!")
            verify(exactly = 0) { dbWeatherForecastDao.insertDbWeatherForecast(any()) }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun fetchWeatherAndPersistIt_weatherForecastApiSuccessfully_returnsResponseSuccessAndInsertToDatabase() {
        runTest {
            coEvery {
                weatherForecastApi.getWeatherForecast(
                    emptyString(),
                    0, emptyString()
                )
            } returns FakeNetworkSource.testNetWeatherForecast()

            every { moshi.adapter(WeatherForecast::class.java).toJson(any()) } returns emptyString()
            every { dbWeatherForecastDao.insertDbWeatherForecast(any()) } returns Unit
            every { dateTimeProvider.getCurrentTimestamp() } returns System.currentTimeMillis()

            val item = weatherRepository.fetchWeatherAndPersistIt(
                emptyString(),
                0,
                emptyString()
            ).first()

            assertTrue(item is Response.Success)
            assertTrue((item as Response.Success).data.city.id == 1580578L)
            assertTrue((item).data.dayWeathers[0].datetime == 1658980800L)
            assertTrue((item).data.dayWeathers[0].weather[0].description == "light rain")
            verify(exactly = 1) { dbWeatherForecastDao.insertDbWeatherForecast(any()) }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getWeatherForecast_getWeatherForecastFromCacheThrowsError_fetchWeatherFromNetwork() {
        runTest {
            every {
                dbWeatherForecastDao.getDbWeatherForecast(any())
            }.throws(Exception("Fake error!"))
            spyWeatherRepository.getWeatherForecast(
                emptyString(), 0, emptyString()
            ).first()

            verify(exactly = 0) { moshi.adapter(WeatherForecast::class.java) }
            verify(exactly = 1) { spyWeatherRepository.fetchWeatherAndPersistIt(any(), any(), any()) }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getWeatherForecast_parsingWeatherDataFromCacheThrowError_fetchWeatherFromNetwork() {
        runTest {
            every {
                dbWeatherForecastDao.getDbWeatherForecast(any())
            } returns DbWeatherForecast(
                emptyString(), emptyString(), System.currentTimeMillis(), emptyString()
            )
            every {
                moshi.adapter(WeatherForecast::class.java).fromJson(emptyString())
            }.throws(Exception("Error here!"))
            spyWeatherRepository.getWeatherForecast(
                emptyString(), 0, emptyString()
            ).first()

            verify(exactly = 1) { spyWeatherRepository.fetchWeatherAndPersistIt(any(), any(), any()) }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getWeatherForecast_parsingWeatherDataFromCacheReturnNull_fetchWeatherFromNetwork() {
        runTest {
            every {
                dbWeatherForecastDao.getDbWeatherForecast(any())
            } returns DbWeatherForecast(
                emptyString(), emptyString(), System.currentTimeMillis(), emptyString()
            )
            every {
                moshi.adapter(WeatherForecast::class.java).fromJson(emptyString())
            } returns null
            spyWeatherRepository.getWeatherForecast(
                emptyString(), 0, emptyString()
            ).first()

            verify(exactly = 1) { spyWeatherRepository.fetchWeatherAndPersistIt(any(), any(), any()) }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getWeatherForecast_weatherFromCacheIsNotYetOutdated_returnsWeatherFromCache() {
        runTest {
            every {
                dbWeatherForecastDao.getDbWeatherForecast(any())
            } returns DbWeatherForecast(
                emptyString(), emptyString(), System.currentTimeMillis(), emptyString()
            )
            every {
                moshi.adapter(WeatherForecast::class.java).fromJson(emptyString())
            } returns WeatherForecast(City(0,0), emptyList())
            every {
                spyWeatherRepository.isOutdatedWeather(any())
            } returns false
            val item = spyWeatherRepository.getWeatherForecast(
                emptyString(), 0, emptyString()
            ).first()

            verify(exactly = 0) { spyWeatherRepository.fetchWeatherAndPersistIt(any(), any(), any()) }
            assertTrue(item is Response.Success)
            assertTrue((item as Response.Success).data.city.id == 0L)
            assertTrue((item).data.city.timezone == 0L)
            assertTrue((item).data.dayWeathers.isEmpty())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getWeatherForecast_weatherFromCacheIsOutdated_fetchWeatherFromNetwork() {
        runTest {
            every {
                dbWeatherForecastDao.getDbWeatherForecast(any())
            } returns DbWeatherForecast(
                emptyString(), emptyString(), System.currentTimeMillis(), emptyString()
            )
            every {
                moshi.adapter(WeatherForecast::class.java).fromJson(emptyString())
            } returns WeatherForecast(City(0,0), emptyList())
            every {
                spyWeatherRepository.isOutdatedWeather(any())
            } returns true
            val item = spyWeatherRepository.getWeatherForecast(
                emptyString(), 0, emptyString()
            ).first()

            verify(exactly = 1) { spyWeatherRepository.fetchWeatherAndPersistIt(any(), any(), any()) }
        }
    }
}