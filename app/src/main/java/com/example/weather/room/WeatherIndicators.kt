package com.example.weather.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Forecast::class, parentColumns = ["id"], childColumns = ["forecast_id"], onDelete = CASCADE)],
    tableName = "weather_indicators")
data class WeatherIndicators (
    @PrimaryKey(autoGenerate = true)
    var id: Long?,

    var date: String,

    var temperature: Double,

    var clouds: Int,

    @ColumnInfo(name = "wind_speed")
    var windSpeed: Double,

    var humidity: Int,

    var pressure: Double,

    var rain: Double?,

    var state: String,

    @ColumnInfo(name = "forecast_id")
    var forecastID: Long
)