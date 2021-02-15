package com.akipa.dto.request

data class AgregarPlatoADetalleRequest(
    val idPedido: Int,
    val idPlato: Int,
    val cantidad: Int
)