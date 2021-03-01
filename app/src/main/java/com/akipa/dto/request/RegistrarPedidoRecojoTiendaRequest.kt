package com.akipa.dto.request

import com.squareup.moshi.Json

data class RegistrarPedidoRecojoTiendaRequest(
    @Json(name = "id_local")
    val idLocal: Int,
    @Json(name = "id_solicitante")
    val idSolicitante: String,
    @Json(name = "fecha_recojo")
    val fecha: String,
    @Json(name = "hora_recojo")
    val hora: String
)