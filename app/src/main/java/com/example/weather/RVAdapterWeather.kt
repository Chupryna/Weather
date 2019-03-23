package com.example.weather

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.weather.data.model.ListItem
import kotlinx.android.synthetic.main.weather_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class RVAdapterWeather(list: List<ListItem>) : RecyclerView.Adapter<RVAdapterWeather.WeatherViewHolder>() {

    private val listWeather = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)
        return WeatherViewHolder(view)
    }

    override fun getItemCount(): Int {
       return listWeather.size
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val item = listWeather[position]
        val date = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).parse(item.dtTxt)

        holder.textDate.text = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(date)
        holder.textTemperature.text = String.format("%.1f°C", item.main.temp - 273.15)
        holder.textWeatherState.text = item.weather!![0].description
        holder.textClouds.text = String.format("%d%%", item.clouds.all)
        holder.textWindSpeed.text = String.format("%.2f м/с", item.wind.speed)
        holder.textHumidity.text = String.format("%d%%", item.main.humidity)
        holder.textPressure.text = String.format("%.0f mPa", item.main.pressure)
        if (item.rain != null)
            holder.textRain.text = String.format("%.2f mm", item.rain.H)
        else
            holder.textRain.text = "0 мм"
    }

    class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDate: TextView = itemView.textDate
        val textTemperature: TextView = itemView.textTemperature
        val textWeatherState: TextView = itemView.textWeatherState
        val textClouds: TextView = itemView.textCloudsValue
        val textWindSpeed: TextView = itemView.textWindSpeedValue
        val textHumidity: TextView = itemView.textHumidityValue
        val textPressure: TextView = itemView.textPressureValue
        val textRain: TextView = itemView.textRainValue
    }
}