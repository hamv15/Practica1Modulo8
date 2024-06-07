package com.hamv15.practica2.application

import android.app.Application
import com.hamv15.practica2.data.CarRepository
import com.hamv15.practica2.data.remote.RetrofitHelper

class Practica2App: Application() {

    private  val retrofit by lazy {
        RetrofitHelper().getRetrofit()
    }

    val repository by lazy {
        CarRepository(retrofit)
    }
}