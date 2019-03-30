package com.example.weather.data.model

import com.google.gson.annotations.SerializedName

data class City(@SerializedName("id")
                val id: Int = 0,
                @SerializedName("city")
                val name: String = "",
                @SerializedName("coord")
                val coord: Coord,
                @SerializedName("country")
                val country: String = "")