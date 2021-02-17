package com.akipa.dto.request

data class DeliveryRequest(
    val idSolicitante: String,
    val direccion: String,
    val referencia: String = "no se especifico"
)