package com.akipa.dto.request

import com.squareup.moshi.Json

data class RegistrarPedidoDeliveryRequest(
    @Json(name = "id_solicitante")
    val idSolicitante: String,
    @Json(name = "direccion_entrega")
    val direccion: String,
    @Json(name = "referencia_entrega")
    val referencia: String
)