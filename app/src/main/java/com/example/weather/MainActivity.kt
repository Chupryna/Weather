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
import com.example.weather.room.WeatherDatabase
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


       /* val weatherDB = WeatherDatabase.getInstance(this@MainActivity)
        val city = Forecast(null, "Lviv", 1234)
        val wInd = WeatherIndicators(null, "2019", 7.8, 88, 1.3, 50,
            1020.0, null, "Хмарно", null)
        val wIn1 = WeatherIndicators(null, "2019", 7.9, 100, 8.3, 70,
            1080.0, null, "Хмарно", null)
        val weatherIndicators = listOf(wInd, wIn1)

        weatherDB!!.forecastWeatherDao().insertCityAndWeatherIndicators(city, weatherIndicators)

        val res = weatherDB.forecastWeatherDao().getWeatherIndicators()
        println(res)*/
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

        val listOfCity: MutableList<String> = mutableListOf("Lviv")
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
        val city = editCity.text.toString().trim()

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

                val weatherDB = WeatherDatabase.getInstance(this@MainActivity)
                val forecast = weatherDB!!.forecastWeatherDao().getForecastByCity(city)
                if (forecast.isEmpty())
                    println("132")
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