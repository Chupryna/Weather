package com.example.weather.room

import androidx.room.Embedded
import androidx.room.Relation

data class ForecastWithWeatherIndicators(
    @Embedded
    var forecast: Forecast,

    @Relation(parentColumn = "id", entityColumn = "forecast_id")
    var weatherIndicators: List<WeatherIndicators>
)

