package com.ngocthanhnguyen.core.database.di

import com.ngocthanhnguyen.core.database.AppDatabase
import com.ngocthanhnguyen.core.database.createAppDatabase
import org.koin.dsl.module

val coreDatabaseModule = module {
    single { createAppDatabase(get()) }
    single { (get() as AppDatabase).dbWeatherForecastDao() }
}