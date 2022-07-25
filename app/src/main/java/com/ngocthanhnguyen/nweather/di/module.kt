package com.ngocthanhnguyen.nweather.di

import com.ngocthanhnguyen.nweather.data.disk.AppDatabase
import com.ngocthanhnguyen.nweather.data.disk.createAppDatabase
import com.ngocthanhnguyen.nweather.data.network.NWeatherApiClient
import com.ngocthanhnguyen.nweather.data.network.api.WeatherForecastApi
import com.ngocthanhnguyen.nweather.repository.WeatherRepository
import com.ngocthanhnguyen.nweather.screen.WeatherForecastViewModel
import com.ngocthanhnguyen.nweather.util.DateTimeFormatter
import com.ngocthanhnguyen.nweather.util.DateTimeProvider
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module

val MOSHI_FOR_ROOM = StringQualifier("MOSHI_FOR_ROOM")

fun createMoshiForRoom(): Moshi? {
    return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
}

val appModule = module {
    single(MOSHI_FOR_ROOM) { createMoshiForRoom() }
    single { WeatherRepository(get(), get(), get(MOSHI_FOR_ROOM), get()) }
    single { DateTimeProvider() }
    single { NWeatherApiClient() }
    single { (get() as NWeatherApiClient).createService(WeatherForecastApi::class.java) }
    single { WeatherForecastViewModel(get()) }
    single { createAppDatabase(get()) }
    single { (get() as AppDatabase).apiCallResultDao() }
    single { DateTimeFormatter() }
}