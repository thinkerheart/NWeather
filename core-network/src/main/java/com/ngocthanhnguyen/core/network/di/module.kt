package com.ngocthanhnguyen.core.network.di

import com.ngocthanhnguyen.core.network.NWeatherApiClient
import com.ngocthanhnguyen.core.network.api.WeatherForecastApi
import org.koin.dsl.module

val coreNetworkModule = module {
    single { NWeatherApiClient() }
    single { (get() as NWeatherApiClient).createService(WeatherForecastApi::class.java) }

}