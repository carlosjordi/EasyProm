package com.akipa.dto

import com.squareup.moshi.Json

data class DeliveryResponse(val mensaje: String, @Json(name = "id_pedido") val idPedido: Int)