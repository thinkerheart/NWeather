package com.ngocthanhnguyen.core.data.di

import com.ngocthanhnguyen.core.data.repository.WeatherRepository
import com.ngocthanhnguyen.core.domain.repository.IWeatherRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.bind
import org.koin.dsl.module

val MOSHI_FOR_ROOM = StringQualifier("MOSHI_FOR_ROOM")

fun createMoshiForRoom(): Moshi? {
    return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
}

val coreDataModule = module {
    single(MOSHI_FOR_ROOM) { createMoshiForRoom() }
    single {
        WeatherRepository(
            get(),
            get(),
            get(MOSHI_FOR_ROOM),
            get()
        )
    } bind IWeatherRepository::class
}