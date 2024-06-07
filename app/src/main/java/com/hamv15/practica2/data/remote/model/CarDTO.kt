package com.hamv15.practica2.data.remote.model

import com.google.gson.annotations.SerializedName

data class CarDTO(

    @SerializedName("idAuto")
    var idAuto : String? = null,

    @SerializedName("marca")
    var marca : String? = null,

    @SerializedName("modelo")
    var modelo : String? = null,

    @SerializedName("anno")
    var anno : Int? = null,

    @SerializedName("imagen")
    var imagen : String? = null



)
