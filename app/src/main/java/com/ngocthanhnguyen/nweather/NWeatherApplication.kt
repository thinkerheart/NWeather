package com.ngocthanhnguyen.nweather

import android.app.Application
import com.ngocthanhnguyen.nweather.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class NWeatherApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@NWeatherApplication)
            modules(appModule)
        }
    }
}