package com.example.weather.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class ForecastWeatherDao {

   // abstract fun forecastDao(): ForecastDao
   // abstract fun weatherIndicatorsDao(): WeatherIndicatorsDao

    @Query("SELECT * FROM forecast WHERE city = :city")
    abstract fun getForecastByCity(city: String): List<Forecast>

    @Insert
    abstract fun insertForecast(forecast: Forecast): Long




    @Query("SELECT * FROM weather_indicators")
    abstract fun getWeatherIndicators(): List<WeatherIndicators>

    @Insert
    abstract fun insertWeatherIndicators(weatherIndicators: List<WeatherIndicators>) : List<Long>


    @Transaction
    open fun insertCityAndWeatherIndicators(forecast: Forecast, weatherIndicators: List<WeatherIndicators>) {
        val forecastIDId = insertForecast(forecast)
        for (item in weatherIndicators)
            item.forecastID = forecastIDId
        insertWeatherIndicators(weatherIndicators)
    }
}