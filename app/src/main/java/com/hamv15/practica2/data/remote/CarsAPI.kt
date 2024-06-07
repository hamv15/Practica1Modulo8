package com.hamv15.practica2.data.remote

import com.hamv15.practica2.data.remote.model.CarDTO
import com.hamv15.practica2.data.remote.model.CarDetailDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface CarsAPI{
    //https://private-516dc-autosapi.apiary-mock.com/listAutos
    @GET
    fun getCars(
        @Url url: String?
    ): Call<List<CarDTO>>

    //Para generar endpoints del estilo
//https://private-516dc-autosapi.apiary-mock.com/describeAuto/3
    @GET("describeAuto/{id}")
    fun getCarDetail(
        @Path("id") id: String?
        //@Path("name") name: String?
    ): Call<CarDetailDTO>
}

