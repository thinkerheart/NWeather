package com.ngocthanhnguyen.nweather.data.disk

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ngocthanhnguyen.nweather.data.disk.dao.APICallResultDao
import com.ngocthanhnguyen.nweather.model.APICallResult

@Database(
    entities = [
        APICallResult::class
    ],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun apiCallResultDao(): APICallResultDao
}