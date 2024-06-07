package com.hamv15.practica2.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.hamv15.practica2.data.remote.model.CarDTO
import com.hamv15.practica2.databinding.CarElementBinding

class CarViewHolder(private val binding: CarElementBinding): RecyclerView.ViewHolder(binding.root) {

    val ivThumbnail = binding.ivThumbnail

    fun bind(car: CarDTO){

        binding.apply {
            tvMaker.text = car.marca
            tvModel.text = car.modelo
            tvYear.text = car.anno.toString()
        }
    }

}