package com.ngocthanhnguyen.feature_city_weather_forecast

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.ngocthanhnguyen.core.common.util.DateTimeFormatter
import com.ngocthanhnguyen.core.common.util.defaultIfNull
import com.ngocthanhnguyen.core.domain.entity.Response
import com.ngocthanhnguyen.feature_city_weather_forecast.databinding.FragmentWeatherBinding
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class CityWeatherFragment : Fragment(R.layout.fragment_weather) {
    private val cityWeatherViewModel: CityWeatherViewModel by inject()
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
            CityWeatherAdapter(emptyList(), it, dateTimeFormatter, cityWeatherViewModel)
        }

        fragmentWeatherBinding?.rcvWeather?.adapter = weatherAdapter

        cityWeatherViewModel.weatherForecast.observe(viewLifecycleOwner) {
            when (it) {
                is Response.Uninitialized -> {
                    weatherAdapter?.dayWeatherList = emptyList()
                    weatherAdapter?.notifyDataSetChanged()
                }
                is Response.Loading -> {
                    // may be show UI loader
                }
                is Response.Success -> {
                    weatherAdapter?.dayWeatherList = it.data.dayWeathers
                    weatherAdapter?.notifyDataSetChanged()
                }
                is Response.Error -> {
                    cityWeatherViewModel.error.value = it.errorValue
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
                cityWeatherViewModel.searchKeyword.value = it
                if (it.length >= 3) {
                    cityWeatherViewModel.getWeatherForecast(
                        lifecycleScope,
                        cityWeatherViewModel.searchKeyword.value.defaultIfNull(),
                        7,
                        com.ngocthanhnguyen.core.common.enum.TemperatureUnit.CELSIUS.code
                    )
                } else {
                    cityWeatherViewModel.resetState()
                }
            }
        }
    }
    private fun initErrorObserver() {
        cityWeatherViewModel.error.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}