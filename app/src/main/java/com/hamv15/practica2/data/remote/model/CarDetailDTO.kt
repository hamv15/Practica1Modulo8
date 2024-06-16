package com.hamv15.practica2.data.remote.model

import com.google.gson.annotations.SerializedName

data class CarDetailDTO(

    @SerializedName("idAuto")
    var idAuto : String? = null,

    @SerializedName("marca")
    var marca : String? = null,

    @SerializedName("modelo")
    var modelo : String? = null,

    @SerializedName("anno")
    var anno : Int? = null,

    @SerializedName("version")
    var version : String? = null,

    @SerializedName("motor")
    var motor : String? = null,

    @SerializedName("potencia")
    var potencia : String? = null,

    @SerializedName("transmision")
    var transmision : String? = null,

    @SerializedName("imagen")
    var imagen : String? = null,

    @SerializedName("videoUrl")
    var videoUrl : String ? = null,

    @SerializedName("concesionario")
    var concesionario: ConcesionarioDTO? = null

)
