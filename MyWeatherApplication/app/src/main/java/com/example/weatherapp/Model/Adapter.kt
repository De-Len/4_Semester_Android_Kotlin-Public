package com.example.weatherapp.Model

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R

class Adapter(diffCallback: DiffCallback) : ListAdapter<WeatherEntry, Adapter.ViewHolder>(diffCallback) {
    var temperatureFormat = "%.0f °C"
    var temperatureFormatDimension = 273.15

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(weatherEntry: WeatherEntry) {
            itemView.findViewById<TextView>(R.id.date).text = weatherEntry.dt_txt
            val tempCel = weatherEntry.main.temp - temperatureFormatDimension
//            itemView.findViewById<TextView>(R.id.temp).text = String.format("%.0f °C", tempCel)
            itemView.findViewById<TextView>(R.id.temp).text = String.format(temperatureFormat, tempCel)

            val iconUrl = "https://openweathermap.org/img/wn/${weatherEntry.weather[0].icon}@2x.png"
            Glide.with(itemView.context)
                .load(iconUrl)
                .into(itemView.findViewById(R.id.temp_icon))
        }
    }

    fun updateData(temperatureFormat: String, temperatureFormatDimension: Double) {
        this.temperatureFormat = temperatureFormat
        this.temperatureFormatDimension = temperatureFormatDimension

        notifyDataSetChanged()  // Уведомляем адаптер об изменениях
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.forecast_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecastItem = getItem(position)
        holder.bind(forecastItem)
    }
}

class DiffCallback : DiffUtil.ItemCallback<WeatherEntry>() {
    override fun areItemsTheSame(oldItem: WeatherEntry, newItem: WeatherEntry): Boolean {
        return oldItem.dt_txt == newItem.dt_txt
    }

    override fun areContentsTheSame(oldItem: WeatherEntry, newItem: WeatherEntry): Boolean {
        return oldItem == newItem
    }
}

