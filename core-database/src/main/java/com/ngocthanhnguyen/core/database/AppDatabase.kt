package com.ngocthanhnguyen.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ngocthanhnguyen.core.database.dao.APICallResultDao
import com.ngocthanhnguyen.core.database.model.APICallResult

@Database(
    entities = [
        APICallResult::class
    ],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun apiCallResultDao(): APICallResultDao
}