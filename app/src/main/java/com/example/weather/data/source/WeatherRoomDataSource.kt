package com.example.weather.data.source

import android.content.Context
import com.example.weather.data.model.ListItem
import com.example.weather.room.Forecast
import com.example.weather.room.ForecastWithWeatherIndicators
import com.example.weather.room.WeatherDatabase
import com.example.weather.room.WeatherIndicators

class WeatherRoomDataSource(private val context: Context) : RoomDataSource {

    override fun saveForecast(forecastWithWeatherIndicators: ForecastWithWeatherIndicators) {
        val weatherDB = WeatherDatabase.getInstance(context)
        val forecastList = weatherDB!!.forecastWeatherDao().getForecastByCity(forecastWithWeatherIndicators.forecast.city)
        if (forecastList.isEmpty()) {
            weatherDB.forecastWeatherDao().insertForecastAndWeatherIndicators(
                forecastWithWeatherIndicators.forecast,
                forecastWithWeatherIndicators.weatherIndicators
            )
        } else if (forecastList[0].time != forecastWithWeatherIndicators.forecast.time) {
            val forecast = Forecast(forecastList[0].id, forecastList[0].city, forecastWithWeatherIndicators.forecast.time)
            weatherDB.forecastWeatherDao().updateForecastAndWeatherIndicators(forecast, forecastWithWeatherIndicators.weatherIndicators)
        }
        WeatherDatabase.destroyInstance()
    }

    override fun loadWeatherFromMemory(city: String): ForecastWithWeatherIndicators? {
        val weatherDB = WeatherDatabase.getInstance(context)
        val forecastByCity = weatherDB!!.forecastWeatherDao().getForecastByCity(city)
        var forecastWithWeatherIndicators: ForecastWithWeatherIndicators? = null
        if (forecastByCity.isNotEmpty()) {
            forecastWithWeatherIndicators = weatherDB.forecastWeatherDao().getForecastWithWeatherIndicators(forecastByCity[0].id!!)
        }
        WeatherDatabase.destroyInstance()
        return forecastWithWeatherIndicators
    }

    override fun convertListItemToForecastWithWeatherIndicators(list: List<ListItem>, city: String): ForecastWithWeatherIndicators {
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
}