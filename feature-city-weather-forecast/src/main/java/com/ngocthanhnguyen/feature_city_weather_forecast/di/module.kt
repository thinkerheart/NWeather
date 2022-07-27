package com.ngocthanhnguyen.feature_city_weather_forecast.di

import com.ngocthanhnguyen.feature_city_weather_forecast.CityWeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureCityWeatherForecastModule = module {
    viewModel { CityWeatherViewModel(get()) }
}