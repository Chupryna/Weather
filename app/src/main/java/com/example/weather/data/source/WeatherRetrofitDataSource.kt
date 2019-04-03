package com.example.weather.data.source

import com.example.weather.data.model.Weather
import com.example.weather.data.source.retrofit_api.OpenWeatherMapAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRetrofitDataSource : RetrofitDataSource{

    private val openWeatherMapAPI: OpenWeatherMapAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        openWeatherMapAPI = retrofit.create(OpenWeatherMapAPI::class.java)
    }

    override fun getWeatherByID(id: Long, callBack: RetrofitDataSource.LoadWeatherCallBack) {
        val call: Call<Weather> = openWeatherMapAPI.loadWeatherByID(id, OpenWeatherMapAPI.API_KEY)
        executeCall(call, callBack)
    }

    override fun getWeatherByName(city: String, callBack: RetrofitDataSource.LoadWeatherCallBack) {
        val call: Call<Weather> = openWeatherMapAPI.loadWeatherByName(city, OpenWeatherMapAPI.API_KEY)
        executeCall(call, callBack)
    }

    private fun executeCall(call: Call<Weather>, callBack: RetrofitDataSource.LoadWeatherCallBack) {
        call.enqueue(object : Callback<Weather> {
            override fun onFailure(call: Call<Weather>, t: Throwable) {
                callBack.onFailure()
            }

            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                if (response.body() != null)
                    callBack.onWeatherLoaded(response.body()!!.list!!)
                else
                    callBack.onFailure()
            }
        })
    }
}