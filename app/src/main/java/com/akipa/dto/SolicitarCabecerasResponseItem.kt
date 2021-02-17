package com.akipa.dto

data class SolicitarCabecerasResponseItem(
    val direccion: String,
    val estado: Int, // 0 en espera | 1 aceptado | 2 rechazado
    val fecha: String,
    val hora: String,
    val id: Int,
    val local: String,
    val observacion: String?,
    val referencia: String,
    val tipo_pedido: Int // 1 recojo en tienda | 2 delivery
)