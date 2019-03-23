package com.example.weather.data.model

import com.google.gson.annotations.SerializedName

data class Weather(@SerializedName("cod")
                   val cod: String = "",
                   @SerializedName("message")
                   val message: Double = 0.0,
                   @SerializedName("cnt")
                   val cnt: Int = 0,
                   @SerializedName("list")
                   val list: List<ListItem>?,
                   @SerializedName("city")
                   val city: City
)