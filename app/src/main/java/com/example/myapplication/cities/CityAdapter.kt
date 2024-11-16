package com.example.myapplication.cities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.CityItemBinding

class CityAdapter(private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<CityViewHolder>() {
    private var data: List<String> = emptyList()

    fun setData(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        return CityViewHolder(
            CityItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val item = data.getOrNull(position)
        with(holder.binding) {
            city.text = item ?: ""
        }
        holder.binding.city.setOnClickListener {
            item?.let {
                onClick(item)
            }
        }
    }
}

class CityViewHolder(val binding: CityItemBinding) : RecyclerView.ViewHolder(binding.root)