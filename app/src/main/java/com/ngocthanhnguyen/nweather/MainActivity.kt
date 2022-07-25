package com.ngocthanhnguyen.nweather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.ngocthanhnguyen.nweather.screen.WeatherForecastFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<WeatherForecastFragment>(R.id.fcvWeather)
            }
        }
    }
}
