package com.ngocthanhnguyen.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "db_weather_forecast")
class DbWeatherForecast (
    @PrimaryKey
    @ColumnInfo(name = "city_id") val cityId: String,
    @ColumnInfo(name = "city_name") val cityName: String,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
    @ColumnInfo(name = "weather_data") val weatherData: String
)