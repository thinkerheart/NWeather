package com.ngocthanhnguyen.core.network.fake

import com.ngocthanhnguyen.core.network.NetworkWeatherDataSource
import com.ngocthanhnguyen.core.network.model.NetWeatherForecast
import com.squareup.moshi.Moshi

class FakeNetworkDataSource(private val moshi: Moshi) : NetworkWeatherDataSource {
    override suspend fun getWeatherForecast(
        cityName: String,
        numberOfForecastDays: Int,
        units: String
    ): NetWeatherForecast? {
        return moshi.adapter(NetWeatherForecast::class.java).fromJson(FakeDataSource.sampleNetWeatherForecast)
    }
}