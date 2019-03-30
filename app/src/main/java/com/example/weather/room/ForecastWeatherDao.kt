package com.example.weather.room

import androidx.room.*

@Dao
abstract class ForecastWeatherDao {

   // abstract fun forecastDao(): ForecastDao
   // abstract fun weatherIndicatorsDao(): WeatherIndicatorsDao

    //ForecastDao
    @Query("SELECT * FROM forecast WHERE city = :city")
    abstract fun getForecastByCity(city: String): List<Forecast>

    @Insert
    abstract fun insertForecast(forecast: Forecast): Long

    @Update
    abstract fun updateForecast(forecast: Forecast): Int


    //WeatherIndicatorsDao
    @Query("SELECT * FROM weather_indicators")
    abstract fun getWeatherIndicators(): List<WeatherIndicators>

    @Insert
    abstract fun insertWeatherIndicators(weatherIndicators: List<WeatherIndicators>): List<Long>

    @Update
    abstract fun updateWeatherIndicators(weatherIndicators: List<WeatherIndicators>): Int

    @Query("DELETE FROM weather_indicators WHERE forecast_id = :forecastID")
    abstract fun deleteWeatherIndicatorsByForecastID(forecastID: Long): Int


    @Transaction
    open fun insertForecastAndWeatherIndicators(forecast: Forecast, weatherIndicators: List<WeatherIndicators>) {
        val forecastIDId = insertForecast(forecast)
        for (item in weatherIndicators)
            item.forecastID = forecastIDId
        insertWeatherIndicators(weatherIndicators)
    }

    @Transaction
    open fun updateForecastAndWeatherIndicators(forecast: Forecast, weatherIndicators: List<WeatherIndicators>) {
        updateForecast(forecast)
        deleteWeatherIndicatorsByForecastID(forecast.id!!)

        for (item in weatherIndicators)
            item.forecastID = forecast.id!!
        insertWeatherIndicators(weatherIndicators)
    }
}