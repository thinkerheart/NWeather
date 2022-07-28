package com.ngocthanhnguyen.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ngocthanhnguyen.core.database.dao.DbWeatherForecastDao
import com.ngocthanhnguyen.core.database.model.DbWeatherForecast

@Database(
    entities = [
        DbWeatherForecast::class
    ],
    version = 2
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun dbWeatherForecastDao(): DbWeatherForecastDao
}