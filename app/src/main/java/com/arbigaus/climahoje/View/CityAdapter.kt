package com.arbigaus.climahoje.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arbigaus.climahoje.Model.City
import com.arbigaus.climahoje.Model.DataStore
import com.arbigaus.climahoje.R

class CityAdapter(private var cities: MutableList<City>) : RecyclerView.Adapter<CityAdapter.CityHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rcv_cities, parent, false)
        return CityHolder(view)
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        val city = cities[position]
        holder.txtName.text = city.name

        holder.btnStar.isClickable = true
        holder.btnStar.setOnClickListener {
            DataStore.updateCity(
                City(name = city.name, stared = true),
                position
            )
            notifyDataSetChanged()
        }
        if (city.stared) {
            holder.btnStar.setImageResource(android.R.drawable.btn_star_big_on)
        }
        else {
            holder.btnStar.setImageResource(android.R.drawable.btn_star_big_off)
        }
    }

    fun back() {

    }

    inner class CityHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtName: TextView = view.findViewById(R.id.cityName)
        var btnStar: ImageView = view.findViewById(R.id.btnStar)
    }
}