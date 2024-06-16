package com.hamv15.practica2.data.remote.model

import com.google.gson.annotations.SerializedName

data class ConcesionarioDTO(
    @SerializedName("nombreConcesionario")
    var nombreConcesionario : String? = null,

    @SerializedName("descripcion")
    var descripcion : String? = null,

    @SerializedName("latitud")
    var latitud : Double? = null,

    @SerializedName("longitud")
    var longitud : Double? = null,
)
