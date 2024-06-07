package com.hamv15.practica2.data

import com.hamv15.practica2.data.remote.CarsAPI
import com.hamv15.practica2.data.remote.model.CarDTO
import com.hamv15.practica2.data.remote.model.CarDetailDTO
import retrofit2.Call
import retrofit2.Retrofit

class CarRepository(private val retrofit: Retrofit) {

    private val carsAPI: CarsAPI = retrofit.create(CarsAPI::class.java)

    //Ya no es necesario utilizar suspend por como está implementada retrofit. Ya lo tiene
    fun getCars(url: String?): Call<List<CarDTO>> {
        return carsAPI.getCars(url)
    }

    suspend fun getCarDetail(id: String?): Call<CarDetailDTO> {
        return carsAPI.getCarDetail(id)

    }
}