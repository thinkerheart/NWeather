package com.ngocthanhnguyen.core.network.mapper

import com.ngocthanhnguyen.core.common.util.emptyString
import com.ngocthanhnguyen.core.domain.entity.*
import com.ngocthanhnguyen.core.network.model.*
import org.junit.Assert.*

import org.junit.Test

class NetWeatherForecastMapperTest {

    @Test
    fun toWeather() {
        var netWeather: NetWeather? = null
        var weather = Weather(emptyString())
        assertEquals(netWeather.toWeather(), weather)

        netWeather = NetWeather(null, null, "rain", null)
        weather = Weather("rain")
        assertEquals(netWeather.toWeather(), weather)

        netWeather = NetWeather(null, null, null, null)
        weather = Weather(emptyString())
        assertEquals(netWeather.toWeather(), weather)
    }

    @Test
    fun toWeathers() {
        var netWeathers: List<NetWeather>? = null
        val weathers = mutableListOf<Weather>()
        assertTrue(netWeathers.toWeathers().isEmpty())

        netWeathers = mutableListOf()
        netWeathers.add(NetWeather(1, "main", "rain", "icon rain"))
        netWeathers.add(NetWeather(2, "main1", "wind", "icon wind"))
        weathers.add(Weather("rain"))
        weathers.add(Weather("wind"))
        assertEquals(netWeathers.toWeathers(), weathers )
    }

    @Test
    fun toTemp() {
        var netTemp: NetTemp? = null
        var temp = Temp(0.0)
        assertEquals(netTemp.toTemp(), temp)

        netTemp = NetTemp(15.5, 0.0, 0.0, 0.0, 0.0, 0.0)
        temp = Temp(15.5)
        assertEquals(netTemp.toTemp(), temp)

        netTemp = NetTemp(null, 0.0, 0.0, 0.0, 0.0, 0.0)
        temp = Temp(0.0)
        assertEquals(netTemp.toTemp(), temp)
    }

    @Test
    fun toDayWeather() {
        var netDayWeather: NetDayWeather? = null
        var dayWeather = DayWeather(0, Temp(0.0), 0, 0, emptyList())
        assertEquals(netDayWeather.toDayWeather(), dayWeather)

        val netWeathers = mutableListOf<NetWeather>()
        netWeathers.add(NetWeather(1, "main", "rain", "icon rain"))
        netWeathers.add(NetWeather(2, "main1", "wind", "icon wind"))
        val netTemp = NetTemp(15.5, null, null, null, null, null)

        netDayWeather = NetDayWeather(
            11323453,
            null,
            null,
            netTemp,
            null,
            25,
            45,
            netWeathers,
            null,
            null,
            null,
            null,
            null,
            null
        )
        dayWeather = DayWeather(
            11323453,
            netTemp.toTemp(),
            25,
            45,
            netWeathers.toWeathers()
        )
        assertEquals(netDayWeather.toDayWeather(), dayWeather)
    }

    @Test
    fun toDayWeathers() {
        var netDayWeathers: List<NetDayWeather>? = null
        val dayWeathers = mutableListOf<DayWeather>()
        assertTrue(netDayWeathers.toDayWeathers().isEmpty())

        netDayWeathers = mutableListOf()

        val netWeathers = mutableListOf<NetWeather>()
        netWeathers.add(NetWeather(1, "main", "rain", "icon rain"))
        netWeathers.add(NetWeather(2, "main1", "wind", "icon wind"))
        val netTemp = NetTemp(15.5, null, null, null, null, null)
        val netDayWeather = NetDayWeather(
            11323453,
            null,
            null,
            netTemp,
            null,
            25,
            45,
            netWeathers,
            null,
            null,
            null,
            null,
            null,
            null
        )
        val dayWeather = DayWeather(
            11323453,
            netTemp.toTemp(),
            25,
            45,
            netWeathers.toWeathers()
        )

        netDayWeathers.add(netDayWeather)
        dayWeathers.add(dayWeather)

        assertEquals(netDayWeathers.toDayWeathers(), dayWeathers)
    }

    @Test
    fun toCity() {
        var netCity: NetCity? = null
        var city = City(0, 0)
        assertEquals(netCity.toCity(), city)

        netCity = NetCity(1, null, null, null, null, 154789654)
        city = City(1, 154789654)
        assertEquals(netCity.toCity(), city)

        netCity = NetCity(null, null, null, null, null, null)
        city = City(0, 0)
        assertEquals(netCity.toCity(), city)
    }

    @Test
    fun toWeatherForecast() {
        var netWeatherForecast: NetWeatherForecast? = null
        var weatherForecast = WeatherForecast(City(0, 0), emptyList())
        assertEquals(netWeatherForecast.toWeatherForecast(), weatherForecast)

        val netWeathers = mutableListOf<NetWeather>()
        netWeathers.add(NetWeather(1, "main", "rain", "icon rain"))
        netWeathers.add(NetWeather(2, "main1", "wind", "icon wind"))
        val netTemp = NetTemp(15.5, null, null, null, null, null)
        val netDayWeather = NetDayWeather(
            11323453,
            null,
            null,
            netTemp,
            null,
            25,
            45,
            netWeathers,
            null,
            null,
            null,
            null,
            null,
            null
        )
        val netCity = NetCity(1, null, null, null, null, 15264786)
        netWeatherForecast = NetWeatherForecast(
            netCity,
            null,
            null,
            null,
            listOf(netDayWeather)
        )
        weatherForecast = WeatherForecast(
            netCity.toCity(),
            listOf(netDayWeather).toDayWeathers()
        )
        assertEquals(netWeatherForecast.toWeatherForecast(), weatherForecast)
    }
}