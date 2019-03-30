package com.example.weather

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.weather.adapter.RVAdapterWeather
import com.example.weather.data.model.ListItem
import com.example.weather.data.source.DataSource
import com.example.weather.data.source.WeatherDataSource
import com.example.weather.room.Forecast
import com.example.weather.room.WeatherDatabase
import com.example.weather.room.WeatherIndicators
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import java.io.*
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initListeners()
        initAdapters()
    }

    private fun initListeners() {
        btnGetWeather.setOnClickListener { onLoadWeather(it) }
    }

    private fun initAdapters() {
        recyclerShowWeather.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        val listOfCity: MutableList<String> = getListOfCity()
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listOfCity)
        editCity.setAdapter(adapter)
    }

    private fun getListOfCity(): MutableList<String> {
        val inputStream: InputStream = resources.openRawResource(R.raw.city_short2)
        val reader = InputStreamReader(inputStream, StandardCharsets.UTF_16)

        val json = reader.readText()
        val jsonArray = JSONArray(json)

        val listOfCity: MutableList<String> = mutableListOf("Львів")
        /*for (i in 0 until jsonArray.length()) {
            val jsonObject = JSONObject(jsonArray[i].toString())
            val city = "${jsonObject.getString("city")}, ${jsonObject.getString("country")}"
            listOfCity.add(city)
        }*/

        return listOfCity
    }

    private fun onLoadWeather(v: View) {
        hideKeyboard(v)
        showProgress(true)
        val city = editCity.text.toString().trim().toLowerCase()

        if(!isNetworkAvailable()) {
            Toast.makeText(this, R.string.not_network, Toast.LENGTH_SHORT).show()
            return
        }

        val weatherDataSource = WeatherDataSource()
        weatherDataSource.getWeatherByName(city, object: DataSource.LoadWeatherCallBack{
            override fun onWeatherLoaded(list: List<ListItem>) {
                showProgress(false)
                val adapter = RVAdapterWeather(list)
                recyclerShowWeather.adapter = adapter

                saveForecast(list)
            }

            override fun onFailure() {
               showProgress(false)
                Toast.makeText(this@MainActivity, R.string.failed_load_weather, Toast.LENGTH_SHORT).show()
            }

            private fun saveForecast(list: List<ListItem>) {
                val weatherDB = WeatherDatabase.getInstance(this@MainActivity)
                val forecastList = weatherDB!!.forecastWeatherDao().getForecastByCity(city)
                if (forecastList.isEmpty()) {
                    val forecast = Forecast(null, city, list[0].dt)
                    val weatherIndicatorsList = getWeatherIndicatorsList(list)
                    weatherDB.forecastWeatherDao().insertForecastAndWeatherIndicators(forecast, weatherIndicatorsList)
                } else if (forecastList[0].time != list[0].dt) {
                    val forecast = Forecast(forecastList[0].id, forecastList[0].city, list[0].dt)
                    val weatherIndicatorsList = getWeatherIndicatorsList(list)
                    weatherDB.forecastWeatherDao().updateForecastAndWeatherIndicators(forecast, weatherIndicatorsList)
                }
                WeatherDatabase.destroyInstance()
            }

            private fun getWeatherIndicatorsList(list: List<ListItem>): MutableList<WeatherIndicators> {
                val weatherIndicatorsList = mutableListOf<WeatherIndicators>()
                for (item in list) {
                    val weatherIndicators = WeatherIndicators(
                        null,
                        item.dtTxt,
                        item.main.temp,
                        item.clouds.all,
                        item.wind.speed,
                        item.main.humidity,
                        item.main.pressure,
                        item.rain?.H,
                        item.weather!![0].description,
                        0
                    )
                    weatherIndicatorsList.add(weatherIndicators)
                }
                return weatherIndicatorsList
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