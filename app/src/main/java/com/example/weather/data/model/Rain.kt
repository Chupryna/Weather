package com.example.weather.model

import com.google.gson.annotations.SerializedName

data class Rain(@SerializedName("3h")
                val H: Double = 0.0)