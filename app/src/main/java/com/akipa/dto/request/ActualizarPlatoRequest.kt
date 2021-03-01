package com.akipa.dto.request

import com.akipa.utils.Constantes
import com.squareup.moshi.Json

data class ActualizarPlatoRequest(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val descripcion: String,
    val foto: String,
    @Json(name = "base_foto")
    val baseParaFoto: String = Constantes.BASE_PARA_FOTOS
)