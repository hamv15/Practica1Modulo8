package com.hamv15.practica2.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.hamv15.practica2.data.remote.model.CarDTO
import com.hamv15.practica2.databinding.CarElementBinding

class CarAdapter(
    private val  cars: List<CarDTO>,
    private val onCarClicked: (CarDTO) -> Unit
): RecyclerView.Adapter<CarViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val binding = CarElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cars.size
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = cars[position]

        holder.bind(car)

        Glide.with(holder.itemView.context)
        Glide.with(holder.itemView.context)
            .load(car.imagen)
            .into(holder.ivThumbnail)

        holder.itemView.setOnClickListener{
            onCarClicked(car)
        }

    }


}