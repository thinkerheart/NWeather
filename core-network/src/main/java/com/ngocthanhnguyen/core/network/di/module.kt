package com.ngocthanhnguyen.core.network.di

import com.ngocthanhnguyen.core.network.NWeatherApiClient
import com.ngocthanhnguyen.core.network.api.WeatherForecastApi
import com.ngocthanhnguyen.core.network.api.WeatherForecastApiClient
import org.koin.dsl.module

val coreNetworkModule = module {
    single { NWeatherApiClient() }
    single { (get() as NWeatherApiClient).createService(WeatherForecastApi::class.java) }
    single { WeatherForecastApiClient(get()) }

}