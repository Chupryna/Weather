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
import com.example.weather.room.ForecastWithWeatherIndicators
import com.example.weather.room.WeatherDatabase
import com.example.weather.room.WeatherIndicators
import kotlinx.android.synthetic.main.activity_main.*

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
        val listOfCity: MutableList<String> = mutableListOf("Львів", "Київ", "Одеса", "Луцьк")

        /*val inputStream: InputStream = resources.openRawResource(R.raw.city_short2)
        val reader = InputStreamReader(inputStream, StandardCharsets.UTF_16)
        val json = reader.readText()
        val jsonArray = JSONArray(json)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = JSONObject(jsonArray[i].toString())
            val city = "${jsonObject.getString("city")}, ${jsonObject.getString("country")}"
            listOfCity.add(city)
        }*/

        return listOfCity
    }

    private fun onLoadWeather(v: View) {
        val city = editCity.text.toString().trim().toLowerCase()
        hideKeyboard(v)

        if(!isNetworkAvailable()) {
            val forecastWithWeatherIndicators = loadWeatherFromMemory(city)
            if (forecastWithWeatherIndicators != null) {
                val adapter = RVAdapterWeather(forecastWithWeatherIndicators)
                recyclerShowWeather.adapter = adapter
                Toast.makeText(this@MainActivity, R.string.load_weather_from_memory, Toast.LENGTH_LONG).show()
            } else
                Toast.makeText(this@MainActivity, R.string.failed_load_weather, Toast.LENGTH_LONG).show()
            return
        }

        showProgress(true)

        val weatherDataSource = WeatherDataSource()
        weatherDataSource.getWeatherByName(city, object: DataSource.LoadWeatherCallBack{
            override fun onWeatherLoaded(list: List<ListItem>) {
                val forecastWithWeatherIndicators = convertListItemToForecastWithWeatherIndicators(list, city)
                val adapter = RVAdapterWeather(forecastWithWeatherIndicators)
                recyclerShowWeather.adapter = adapter

                showProgress(false)
                saveForecast(forecastWithWeatherIndicators)
            }

            override fun onFailure() {
                val forecastWithWeatherIndicators = loadWeatherFromMemory(city)
                if (forecastWithWeatherIndicators != null) {
                    val adapter = RVAdapterWeather(forecastWithWeatherIndicators)
                    recyclerShowWeather.adapter = adapter
                    Toast.makeText(this@MainActivity, R.string.load_weather_from_memory, Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(this@MainActivity, R.string.failed_load_weather, Toast.LENGTH_SHORT).show()
                showProgress(false)
            }

            private fun saveForecast(forecastWithWeatherIndicators: ForecastWithWeatherIndicators) {
                val weatherDB = WeatherDatabase.getInstance(this@MainActivity)
                val forecastList = weatherDB!!.forecastWeatherDao().getForecastByCity(forecastWithWeatherIndicators.forecast.city)
                if (forecastList.isEmpty()) {
                    weatherDB.forecastWeatherDao().insertForecastAndWeatherIndicators(forecastWithWeatherIndicators.forecast,
                        forecastWithWeatherIndicators.weatherIndicators)
                } else if (forecastList[0].time != forecastWithWeatherIndicators.forecast.time) {
                    val forecast = Forecast(forecastList[0].id, forecastList[0].city, forecastWithWeatherIndicators.forecast.time)
                    weatherDB.forecastWeatherDao().updateForecastAndWeatherIndicators(forecast, forecastWithWeatherIndicators.weatherIndicators)
                }
                WeatherDatabase.destroyInstance()
            }
        })
    }

    private fun convertListItemToForecastWithWeatherIndicators(list: List<ListItem>, city: String): ForecastWithWeatherIndicators {
        val forecast = Forecast(null, city, list[0].dt)
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

        return ForecastWithWeatherIndicators(forecast, weatherIndicatorsList)
    }

    private fun loadWeatherFromMemory(city: String): ForecastWithWeatherIndicators? {
        val weatherDB = WeatherDatabase.getInstance(this)
        val forecastID = weatherDB!!.forecastWeatherDao().getForecastByCity(city)[0].id
        var forecastWithWeatherIndicators: ForecastWithWeatherIndicators? = null
        if (forecastID != null) {
            forecastWithWeatherIndicators = WeatherDatabase.getInstance(this)!!
                .forecastWeatherDao()
                .getForecastWithWeatherIndicators(forecastID)
        }
        WeatherDatabase.destroyInstance()
        return forecastWithWeatherIndicators
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