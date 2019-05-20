package com.kisanhub.test.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kisanhub.test.R
import com.kisanhub.test.view.fragments.WeatherFragment

class WeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, WeatherFragment.newInstance())
                    .commit()
        }
    }
}
