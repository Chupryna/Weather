package com.example.weather.data.model

import com.google.gson.annotations.SerializedName

data class ListItem(@SerializedName("dt")
                    val dt: Int = 0,
                    @SerializedName("main")
                    val main: Main,
                    @SerializedName("weather")
                    val weather: List<WeatherItem>?,
                    @SerializedName("clouds")
                    val clouds: Clouds,
                    @SerializedName("wind")
                    val wind: Wind,
                    @SerializedName("rain")
                    val rain: Rain?,
                    @SerializedName("dt_txt")
                    val dtTxt: String = "")