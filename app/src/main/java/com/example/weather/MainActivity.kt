package com.example.weather

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.weather.data.source.WeatherDataSource
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGetWeather.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val city = editCity.text

        if(!isNetworkAvailable()) {
            Toast.makeText(this, R.string.not_network, Toast.LENGTH_SHORT).show()
            return
        }

        val weatherDataSource = WeatherDataSource()
        weatherDataSource.getWeatherByID(524901)

       // println(city)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.allNetworks
        if (networkInfo.isNotEmpty())
            return true

        return false
    }
}