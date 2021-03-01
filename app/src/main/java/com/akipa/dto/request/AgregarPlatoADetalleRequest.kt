package com.akipa.dto.request

import com.squareup.moshi.Json

data class AgregarPlatoADetalleRequest(
    @Json(name = "id_pedido")
    val idPedido: Int,
    @Json(name = "id_plato")
    val idPlato: Int,
    val cantidad: Int
)