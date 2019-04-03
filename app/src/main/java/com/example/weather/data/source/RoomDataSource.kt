package com.example.weather.data.source

import com.example.weather.data.model.ListItem
import com.example.weather.room.ForecastWithWeatherIndicators

interface RoomDataSource {

    fun saveForecast(forecastWithWeatherIndicators: ForecastWithWeatherIndicators)

    fun loadWeatherFromMemory(city: String): ForecastWithWeatherIndicators?

    fun convertListItemToForecastWithWeatherIndicators(list: List<ListItem>, city: String): ForecastWithWeatherIndicators
}