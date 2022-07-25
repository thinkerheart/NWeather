package com.ngocthanhnguyen.nweather.data.disk

import android.content.Context
import androidx.room.Room

fun createAppDatabase(applicationContext: Context): AppDatabase {
    return Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "database-nweather"
    ).fallbackToDestructiveMigration().build()
}