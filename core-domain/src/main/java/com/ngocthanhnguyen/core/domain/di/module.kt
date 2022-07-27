package com.ngocthanhnguyen.core.domain.di

import com.ngocthanhnguyen.core.domain.usecase.GetCityWeatherUseCase
import org.koin.dsl.module

val coreDomainModule = module {
    single { GetCityWeatherUseCase(get()) }
}