package com.akipa.dto.request

import com.squareup.moshi.Json

data class GestionarPedidoRequest(
    @Json(name = "id_pedido") val idPedido: Int,
    val observacion: String,
    val estado: Int
)