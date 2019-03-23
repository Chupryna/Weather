package com.example.weather.data.source

import com.example.weather.data.model.ListItem

interface DataSource {

    interface LoadWeatherCallBack{
        fun onWeatherLoaded(list: List<ListItem>)
        fun onFailure()
    }

    fun getWeatherByName(city: String, callBack: LoadWeatherCallBack)

    fun getWeatherByID(id: Long, callBack: LoadWeatherCallBack)
}