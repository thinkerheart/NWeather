package com.ngocthanhnguyen.core.network.fake

import com.ngocthanhnguyen.core.network.fake.FakeDataSource.testNetWeatherForecast
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class FakeNetworkDataSourceTest {

    private lateinit var fakeNetworkDataSource: FakeNetworkDataSource

    @Before
    fun setUp() {
        fakeNetworkDataSource = FakeNetworkDataSource(Moshi.Builder().add(KotlinJsonAdapterFactory()).build())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getWeatherForecast_deserialize_json_to_net_weather_forecast() {
        runTest {
            assertEquals(
                testNetWeatherForecast(),
                fakeNetworkDataSource.getWeatherForecast("saigon", 1, "metrics")
            )
        }
    }
}