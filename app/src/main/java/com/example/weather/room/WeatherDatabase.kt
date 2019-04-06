package com.example.weather.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [Forecast::class, WeatherIndicators::class], version = 1)
abstract class WeatherDatabase: RoomDatabase() {

    abstract fun forecastWeatherDao(): ForecastWeatherDao

    companion object {
        private var instance: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase? {
            if (instance == null)
                instance = Room.databaseBuilder(context.applicationContext, WeatherDatabase::class.java, "weather")
                    .build()
            return instance
        }

        fun destroyInstance() {
            instance = null
        }
    }
}