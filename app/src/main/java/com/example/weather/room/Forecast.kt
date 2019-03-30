package com.example.weather.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Forecast(
    @PrimaryKey(autoGenerate = true)
    var id: Long?,

    var city: String,

    var time: Int
)