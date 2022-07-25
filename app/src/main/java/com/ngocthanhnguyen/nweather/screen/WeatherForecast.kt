package com.ngocthanhnguyen.nweather.screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.ngocthanhnguyen.nweather.R
import com.ngocthanhnguyen.nweather.databinding.FragmentWeatherBinding
import com.ngocthanhnguyen.nweather.enum.TemperatureUnit
import com.ngocthanhnguyen.nweather.model.DayWeather
import com.ngocthanhnguyen.nweather.model.Response
import com.ngocthanhnguyen.nweather.model.WeatherForecastResponse
import com.ngocthanhnguyen.nweather.repository.WeatherRepository
import com.ngocthanhnguyen.nweather.util.DateTimeFormatter
import com.ngocthanhnguyen.nweather.util.defaultIfNull
import com.ngocthanhnguyen.nweather.util.emptyString
import com.ngocthanhnguyen.nweather.viewmodel.BaseViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import org.koin.android.ext.android.inject

class WeatherForecastFragment : Fragment(R.layout.fragment_weather) {
    private val weatherForecastViewModel: WeatherForecastViewModel by inject()
    private val dateTimeFormatter: DateTimeFormatter by inject()
    private var fragmentWeatherBinding: FragmentWeatherBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewBinding(view)
        initWeatherAdapter()
        initViewEventListener()
        initErrorObserver()
    }

    override fun onDestroyView() {
        fragmentWeatherBinding = null
        super.onDestroyView()
    }

    private fun initViewBinding(view: View) {
        val binding = FragmentWeatherBinding.bind(view)
        fragmentWeatherBinding = binding
    }
    private fun initWeatherAdapter() {
        val weatherAdapter = activity?.let {
            WeatherAdapter(emptyList(), it, dateTimeFormatter, weatherForecastViewModel)
        }

        fragmentWeatherBinding?.rcvWeather?.adapter = weatherAdapter

        weatherForecastViewModel.weatherForecast.observe(viewLifecycleOwner) {
            when (it) {
                is Response.Uninitialized -> {
                    weatherAdapter?.dayWeatherList = emptyList()
                    weatherAdapter?.notifyDataSetChanged()
                }
                is Response.Loading -> {
                    // may be show UI loader
                }
                is Response.Success -> {
                    weatherAdapter?.dayWeatherList = it.data.weatherOfForecastDays ?: emptyList()
                    weatherAdapter?.notifyDataSetChanged()
                }
                is Response.Error -> {
                    weatherForecastViewModel.error.value = it.errorValue
                }
            }

        }
    }

    private fun initViewEventListener() {
        lifecycleScope.launch {
            callbackFlow {
                val textWatcher = fragmentWeatherBinding?.edtSearch
                    ?.doOnTextChanged { text, _, _, _ -> this.trySend(text.toString()).isSuccess }
                awaitClose{
                    fragmentWeatherBinding?.edtSearch?.removeTextChangedListener(textWatcher)
                }
            }.debounce(2000).collect {
                weatherForecastViewModel.searchKeyword.value = it
                if (it.length >= 3) {
                    weatherForecastViewModel.getWeatherForecast(
                        lifecycleScope,
                        weatherForecastViewModel.searchKeyword.value.defaultIfNull(),
                        7,
                        TemperatureUnit.CELSIUS.code
                    )
                } else {
                    weatherForecastViewModel.resetState()
                }
            }
        }
    }
    private fun initErrorObserver() {
        weatherForecastViewModel.error.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}

class WeatherForecastViewModel(
    private val weatherRepository: WeatherRepository
) : BaseViewModel() {
    private var searchJob: Job? = null
    val weatherForecast = MutableLiveData<Response<WeatherForecastResponse>>()
    val searchKeyword = MutableLiveData<String>()

    fun getWeatherForecast(
        coroutineScope: CoroutineScope, cityName: String, numberOfForecastDays: Int, units: String
    ) {
        searchJob?.cancel()
        resetState()
        searchJob = coroutineScope.launch {
            try {
                weatherForecast.value = Response.Loading()
                weatherRepository.getWeatherForecast(cityName, numberOfForecastDays, units)
                    .flowOn(Dispatchers.IO)
                    .collect {
                        weatherForecast.value = it
                    }
            } catch (e: Exception) {
                error.value = e.localizedMessage
            }
        }
    }

    fun resetState() {
        weatherForecast.value = Response.Uninitialized()
        error.value = null
    }
}

class WeatherAdapter(
    var dayWeatherList: List<DayWeather>,
    private val context: Context,
    private val dateTimeFormatter: DateTimeFormatter,
    private val weatherForecastViewModel: WeatherForecastViewModel
) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

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
        val weatherForecast = weatherForecastViewModel.weatherForecast.value
        if (weatherForecast is Response.Success) {
            val datetime = dayWeather.dateTime
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
            dayWeather.temp?.day.toString()
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
        dayWeather.weather?.let {
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