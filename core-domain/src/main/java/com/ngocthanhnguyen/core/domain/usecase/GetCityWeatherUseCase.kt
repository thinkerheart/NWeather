package com.ngocthanhnguyen.core.domain.usecase

import com.ngocthanhnguyen.core.domain.entity.Response
import com.ngocthanhnguyen.core.domain.entity.WeatherForecast
import com.ngocthanhnguyen.core.domain.repository.IWeatherRepository
import kotlinx.coroutines.flow.Flow

class GetCityWeatherUseCase(
    private val weatherRepository: IWeatherRepository
): BaseUseCase<GetCityWeatherUseCase.P, Response<WeatherForecast>>() {

    data class P(val cityName: String, val numberOfForecastDays: Int, val units: String)

    override suspend fun execute(param: P): Flow<Response<WeatherForecast>> {
        return weatherRepository.getWeatherForecast(
            param.cityName, param.numberOfForecastDays, param.units
        )
    }
}