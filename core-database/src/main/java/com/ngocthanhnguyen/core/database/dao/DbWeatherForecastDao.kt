package com.ngocthanhnguyen.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ngocthanhnguyen.core.database.model.DbWeatherForecast

@Dao
interface DbWeatherForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDbWeatherForecast(dbWeatherForecast: DbWeatherForecast)

    @Query("SELECT * FROM db_weather_forecast WHERE city_name = :cityName")
    fun getDbWeatherForecast(cityName: String): DbWeatherForecast?
}