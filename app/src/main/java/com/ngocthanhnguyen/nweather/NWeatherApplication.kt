package com.ngocthanhnguyen.nweather

import android.app.Application
import com.ngocthanhnguyen.core.common.di.coreCommonModule
import com.ngocthanhnguyen.core.data.di.coreDataModule
import com.ngocthanhnguyen.core.database.di.coreDatabaseModule
import com.ngocthanhnguyen.core.domain.di.coreDomainModule
import com.ngocthanhnguyen.core.network.di.coreNetworkModule
import com.ngocthanhnguyen.feature_city_weather_forecast.di.featureCityWeatherForecastModule
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
            modules(coreDomainModule)
            modules(coreCommonModule)
            modules(coreDataModule)
            modules(coreDatabaseModule)
            modules(coreNetworkModule)
            modules(featureCityWeatherForecastModule)
        }
    }
}