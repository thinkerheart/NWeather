package com.ngocthanhnguyen.core.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ngocthanhnguyen.core.database.dao.DbWeatherForecastDao
import com.ngocthanhnguyen.core.database.model.DbWeatherForecast
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DbWeatherForecastDaoTest {

    private lateinit var dbWeatherForecastDao: DbWeatherForecastDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()
        dbWeatherForecastDao = db.dbWeatherForecastDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun dbWeatherForecastDao_insert_item_weather_forecast_successfully() {
        dbWeatherForecastDao.insertDbWeatherForecast(testDbWeatherForecast("1", "Sai Gon"))
        var dbWeatherForecast = dbWeatherForecastDao.getDbWeatherForecast("Sai Gon")
        assertEquals("1", dbWeatherForecast?.cityId)

        dbWeatherForecastDao.insertDbWeatherForecast(testDbWeatherForecast("2", "Paris"))
        dbWeatherForecast = dbWeatherForecastDao.getDbWeatherForecast("Paris")
        assertEquals("2", dbWeatherForecast?.cityId)
    }

    @Test
    fun dbWeatherForecastDao_insert_item_weather_forecast_replace_primary_key_successfully() {
        dbWeatherForecastDao.insertDbWeatherForecast(testDbWeatherForecast("1", "Sai Gon"))
        dbWeatherForecastDao.insertDbWeatherForecast(testDbWeatherForecast("1", "Ho Chi Minh"))

        var dbWeatherForecast = dbWeatherForecastDao.getDbWeatherForecast("Sai Gon")
        assertEquals(null, dbWeatherForecast?.cityId)

        dbWeatherForecast = dbWeatherForecastDao.getDbWeatherForecast("Ho Chi Minh")
        assertEquals("1", dbWeatherForecast?.cityId)
    }
}

private fun testDbWeatherForecast(cityId: String, cityName: String) = DbWeatherForecast(
    cityId = cityId,
    cityName = cityName,
    updatedAt = 0,
    weatherData = ""
)