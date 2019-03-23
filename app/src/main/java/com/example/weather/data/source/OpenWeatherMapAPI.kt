package com.example.weather.data.source

import com.example.weather.data.model.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapAPI {

    companion object {
        const val API_KEY = "6e18b3b9568889d2a943929d811580c9"
    }

    @GET("/data/2.5/forecast?lang=ua")
     fun loadWeatherByID(@Query("id") id: Long,
                         @Query("APPID") key: String): Call<Weather>

    @GET("/data/2.5/forecast?lang=ua")
     fun loadWeatherByName(@Query("q") city:String,
                           @Query("APPID") key: String): Call<Weather>

}