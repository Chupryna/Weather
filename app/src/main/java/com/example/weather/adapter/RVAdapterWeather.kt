package com.example.weather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.weather.R
import com.example.weather.room.ForecastWithWeatherIndicators
import kotlinx.android.synthetic.main.weather_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class RVAdapterWeather(forecastWithWeatherIndicators: ForecastWithWeatherIndicators) :
    androidx.recyclerview.widget.RecyclerView.Adapter<RVAdapterWeather.WeatherViewHolder>() {

    private val weatherIndicatorsList = forecastWithWeatherIndicators.weatherIndicators

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)
        return WeatherViewHolder(view)
    }

    override fun getItemCount(): Int {
       return weatherIndicatorsList.size
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val item = weatherIndicatorsList[position]
        val date = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).parse(item.date)

        holder.textDate.text = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(date)
        holder.textTemperature.text = String.format("%.1f°C", item.temperature - 273.15)
        holder.textWeatherState.text = item.state
        holder.textClouds.text = String.format("%d%%", item.clouds)
        holder.textWindSpeed.text = String.format("%.2f м/с", item.windSpeed)
        holder.textHumidity.text = String.format("%d%%", item.humidity)
        holder.textPressure.text = String.format("%.0f mPa", item.pressure)
        if (item.rain != null)
            holder.textRain.text = String.format("%.2f mm", item.rain)
        else
            holder.textRain.text = "0 мм"
    }

    class WeatherViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
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