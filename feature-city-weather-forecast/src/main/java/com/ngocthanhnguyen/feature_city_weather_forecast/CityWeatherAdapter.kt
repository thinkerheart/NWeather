package com.ngocthanhnguyen.feature_city_weather_forecast

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ngocthanhnguyen.core.common.util.DateTimeFormatter
import com.ngocthanhnguyen.core.common.util.emptyString
import com.ngocthanhnguyen.core.domain.entity.DayWeather
import com.ngocthanhnguyen.core.domain.entity.Response

class CityWeatherAdapter(
    var dayWeatherList: List<DayWeather>,
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

    private fun bindDateView(holder: ViewHolder, dayWeather: DayWeather) {
        val weatherForecast = cityWeatherViewModel.weatherForecast.value
        if (weatherForecast is Response.Success) {
            val datetime = dayWeather.datetime
            val timezone = weatherForecast.data.city?.timezone
            if (datetime != null && timezone != null) {
                holder.txvDate.text = "${
                    context.getString(R.string.date)
                } ${
                    dateTimeFormatter.epochToLocalDateTimePattern1(datetime, timezone)
                }"
            }
        }
    }
    private fun bindTemperatureView(holder: ViewHolder, dayWeather: DayWeather) {
        holder.txvAverageTemperature.text = "${
            context.getString(R.string.temperature_average)
        } ${
            dayWeather.temp?.dayTemp.toString()
        }${
            context.getString(R.string.celsius)
        }"
    }
    private fun bindPressureView(holder: ViewHolder, dayWeather: DayWeather) {
        holder.txvPressure.text = "${
            context.getString(R.string.pressure)
        } ${
            dayWeather.pressure.toString()
        }"
    }
    private fun bindHumidityView(holder: ViewHolder, dayWeather: DayWeather) {
        holder.txvHumidity.text = "${
            context.getString(R.string.humidity)
        } ${
            dayWeather.humidity.toString()
        }${
            context.getString(R.string.percentage)
        }"
    }
    private fun bindDescriptionView(holder: ViewHolder, dayWeather: DayWeather) {
        holder.txvDescription.text = "${
            context.getString(R.string.description)
        } ${
            getWeatherDescription(dayWeather)
        }"
    }

    private fun getWeatherDescription(dayWeather: DayWeather): String {
        var description = emptyString()
        dayWeather.weather.let {
            if (it.isNotEmpty()) {
                it.forEach {
                    description += if (description.isEmpty())
                        it.description
                    else
                        "${context.getString(R.string.comma)} ${it.description}"
                }
            }
        }
        return description
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txvDate: TextView = itemView.findViewById(R.id.txvDate)
        val txvAverageTemperature: TextView = itemView.findViewById(R.id.txvAverageTemperature)
        val txvPressure: TextView = this. itemView.findViewById(R.id.txvPressure)
        val txvHumidity: TextView = itemView.findViewById(R.id.txvHumidity)
        val txvDescription: TextView = itemView.findViewById(R.id.txvDescription)
    }
}