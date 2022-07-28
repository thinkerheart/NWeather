package com.ngocthanhnguyen.feature_city_weather_forecast

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ngocthanhnguyen.core.common.util.DateTimeFormatter
import com.ngocthanhnguyen.core.domain.entity.Response
import com.ngocthanhnguyen.feature_city_weather_forecast.mapper.toUiWeatherForecast
import com.ngocthanhnguyen.feature_city_weather_forecast.model.UiDayWeather

class CityWeatherAdapter(
    var dayWeatherList: List<UiDayWeather>,
    private val context: Context,
    private val dateTimeFormatter: DateTimeFormatter,
    private val cityWeatherViewModel: CityWeatherViewModel
) : RecyclerView.Adapter<CityWeatherAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weather, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dayWeather = dayWeatherList[position]

        bindDateView(holder, dayWeather)
        bindTemperatureView(holder, dayWeather)
        bindPressureView(holder, dayWeather)
        bindHumidityView(holder, dayWeather)
        bindDescriptionView(holder, dayWeather)
    }

    override fun getItemCount(): Int {
        return dayWeatherList.size
    }

    private fun bindDateView(holder: ViewHolder, uiDayWeather: UiDayWeather) {
        val weatherForecast = cityWeatherViewModel.weatherForecast.value
        if (weatherForecast is Response.Success) {
            val uiWeatherForecast = weatherForecast.data.toUiWeatherForecast(context)
            val datetime = uiDayWeather.datetime
            val timezone = uiWeatherForecast.cityTimezone
            holder.txvDate.text = "${
                context.getString(R.string.date)
            } ${
                dateTimeFormatter.epochToLocalDateTimePattern1(datetime, timezone)
            }"
        }
    }
    private fun bindTemperatureView(holder: ViewHolder, uiDayWeather: UiDayWeather) {
        holder.txvAverageTemperature.text = "${
            context.getString(R.string.temperature_average)
        } ${
            uiDayWeather.dayTemp
        }${
            context.getString(R.string.celsius)
        }"
    }
    private fun bindPressureView(holder: ViewHolder, uiDayWeather: UiDayWeather) {
        holder.txvPressure.text = "${
            context.getString(R.string.pressure)
        } ${
            uiDayWeather.pressure
        }"
    }
    private fun bindHumidityView(holder: ViewHolder, uiDayWeather: UiDayWeather) {
        holder.txvHumidity.text = "${
            context.getString(R.string.humidity)
        } ${
            uiDayWeather.humidity
        }${
            context.getString(R.string.percentage)
        }"
    }
    private fun bindDescriptionView(holder: ViewHolder, uiDayWeather: UiDayWeather) {
        holder.txvDescription.text = "${
            context.getString(R.string.description)
        } ${
            uiDayWeather.description
        }"
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txvDate: TextView = itemView.findViewById(R.id.txvDate)
        val txvAverageTemperature: TextView = itemView.findViewById(R.id.txvAverageTemperature)
        val txvPressure: TextView = this. itemView.findViewById(R.id.txvPressure)
        val txvHumidity: TextView = itemView.findViewById(R.id.txvHumidity)
        val txvDescription: TextView = itemView.findViewById(R.id.txvDescription)
    }
}