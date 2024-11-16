package com.example.myapplication.selectedcity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.myapplication.R
import com.example.myapplication.databinding.ForecastItemBinding
import com.example.myapplication.models.Forecast


@GlideModule
class CustomGlide : AppGlideModule()
class ForecastAdapter : RecyclerView.Adapter<ForecastViewHolder>() {
    private var data: List<Forecast> = emptyList()

    fun setData(data: List<Forecast>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder(
            ForecastItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val item = data.getOrNull(position)
        with(holder.binding) {
            date.text = (item?.time ?: "").toString()
            temp.text = (item?.temperatureMax ?: 0.0f).toString()
            val src: Int = when (item?.weatherCode) {
                0 -> R.drawable.sunny
                in 1..3 -> R.drawable.partly_cloudy
                45, 48 -> R.drawable.cloudy
                61, 63, 65 -> R.drawable.heavy_rain
                95, 96, 99 -> R.drawable.thunderstorm
                71, 73, 75, in 85..86 -> R.drawable.snowfall
                in 66..67 -> R.drawable.snow_rain
                77 -> R.drawable.little_snow
                else -> R.drawable.little_rain
            }

            item.let {
                Glide
                    .with(weatherIcon.context)
                    .load(src)
                    .into(weatherIcon)
            }
        }
    }

    override fun getItemCount(): Int = data.size
}

class ForecastViewHolder(val binding: ForecastItemBinding) : RecyclerView.ViewHolder(binding.root)