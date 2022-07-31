package com.ngocthanhnguyen.core.network.fake

import com.ngocthanhnguyen.core.network.model.*

object FakeNetworkSource {
    fun testNetWeatherForecast(): NetWeatherForecast {
        val netCoord = NetCoord(106.6667, 10.8333)
        val netCity = NetCity(
            1580578,
            "Ho Chi Minh City",
            netCoord,
            "VN",
            0,
            25200
        )
        val netTemp = NetTemp(305.3, 298.22, 305.79, 299.82, 304.26, 298.22)
        val netFeelsLike = NetFeelsLike(311.21, 302.48, 310.44, 299.13)
        val netWeather = NetWeather(500, "Rain","light rain", "10d")
        val netDayWeather = NetDayWeather(
            1658980800,
            1658961658,
            1659007108,
            netTemp,
            netFeelsLike,
            1008,
            62,
            listOf(netWeather),
            3.24,
            200,
            5.76,
            67,
            0.92,
            4.82
        )

        return NetWeatherForecast(netCity, "200", 30.5919246, 1, listOf(netDayWeather))
    }

    val sampleNetWeatherForecast = """
        {
        "city": {
        "id": 1580578,
        "name": "Ho Chi Minh City",
        "coord": {
        "lon": 106.6667,
        "lat": 10.8333
        },
        "country": "VN",
        "population": 0,
        "timezone": 25200
        },
        "cod": "200",
        "message": 30.5919246,
        "cnt": 1,
        "list": [
        {
        "dt": 1658980800,
        "sunrise": 1658961658,
        "sunset": 1659007108,
        "temp": {
        "day": 305.3,
        "min": 298.22,
        "max": 305.79,
        "night": 299.82,
        "eve": 304.26,
        "morn": 298.22
        },
        "feels_like": {
        "day": 311.21,
        "night": 302.48,
        "eve": 310.44,
        "morn": 299.13
        },
        "pressure": 1008,
        "humidity": 62,
        "weather": [
        {
        "id": 500,
        "main": "Rain",
        "description": "light rain",
        "icon": "10d"
        }
        ],
        "speed": 3.24,
        "deg": 200,
        "gust": 5.76,
        "clouds": 67,
        "pop": 0.92,
        "rain": 4.82
        }
        ]
        }
    """.trimIndent()
}