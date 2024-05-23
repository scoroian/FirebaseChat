package com.scoroian.firebasechat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scoroian.firebasechat.databinding.ItemCityBinding

class CityAdapter(private val cities: List<String>, private val onCitySelected: (String) -> Unit) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val binding = ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cities[position]
        holder.binding.cityTextView.text = city
        holder.binding.root.setOnClickListener {
            onCitySelected(city)
        }
    }

    override fun getItemCount(): Int = cities.size

    class CityViewHolder(val binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root)
}
