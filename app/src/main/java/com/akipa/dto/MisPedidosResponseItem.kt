package com.akipa.dto

data class MisPedidosResponseItem(
    val cantidad: Int,
    val descripcion_plato: String,
    val direccion: String,
    val estado: Int,
    val fecha: String,
    val hora: String,
    val id: Int,
    val local: String,
    val nombre_plato: String,
    val observacion: String?,
    val precio_plato: Double,
    val referencia: String,
    val tipo_pedido: Int,
    val url_plato: String?
)