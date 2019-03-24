package com.example.weather

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.weather.data.model.ListItem
import com.example.weather.data.source.DataSource
import com.example.weather.data.source.WeatherDataSource
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGetWeather.setOnClickListener{ onLoadWeather(it) }
        recyclerShowWeather.layoutManager = LinearLayoutManager(this)
    }

    private fun onLoadWeather(v: View) {
        hideKeyboard(v)
        showProgress(true)
        val city = editCity.text.toString()

        if(!isNetworkAvailable()) {
            Toast.makeText(this, R.string.not_network, Toast.LENGTH_SHORT).show()
            return
        }

        val weatherDataSource = WeatherDataSource()
        //weatherDataSource.getWeatherByID(524901)
        weatherDataSource.getWeatherByName(city, object: DataSource.LoadWeatherCallBack{
            override fun onWeatherLoaded(list: List<ListItem>) {
                showProgress(false)
                val adapter = RVAdapterWeather(list)
                recyclerShowWeather.adapter = adapter
            }

            override fun onFailure() {
                showProgress(false)
                Toast.makeText(this@MainActivity, R.string.failed_load_weather, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.allNetworks
        if (networkInfo.isNotEmpty())
            return true

        return false
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showProgress(show: Boolean) {
        if (show)
            containerProgressBar.visibility = View.VISIBLE
        else
            containerProgressBar.visibility = View.GONE
    }
}