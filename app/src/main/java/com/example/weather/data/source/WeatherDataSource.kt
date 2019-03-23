package com.example.weather.data.source

import com.example.weather.model.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherDataSource {

    private val openWeatherMapAPI: OpenWeatherMapAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        openWeatherMapAPI = retrofit.create(OpenWeatherMapAPI::class.java)
    }

    fun getWeatherByID(id: Long) {
        val call: Call<Weather> = openWeatherMapAPI.loadWeatherByID(id, OpenWeatherMapAPI.API_KEY)
        call.enqueue(object: Callback<Weather> {
            override fun onFailure(call: Call<Weather>, t: Throwable) {
                println(t.message)
            }

            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
              if (response.body() != null) {
                  val weather: Weather = response.body()!!
              }
            }

        })
    }

    fun getWeatherByName(city: String) {
        val call: Call<Weather> = openWeatherMapAPI.loadWeatherByName(city, OpenWeatherMapAPI.API_KEY)
        call.equals(object: Callback<Weather> {
            override fun onFailure(call: Call<Weather>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }
}