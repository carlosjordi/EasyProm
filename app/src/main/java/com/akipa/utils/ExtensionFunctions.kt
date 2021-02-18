package com.akipa.utils

import com.akipa.dto.Detalle

private const val SIMBOLO_SOLES = "S/"

/**
 * Se encarga de formatear el precio obtenido
 * agregandole el simbolo de soles
 */
fun Double.formatearPrecio(): String {
    return "$SIMBOLO_SOLES$this"
}

fun List<Detalle>.obtenerCosteTotal(): String {
    var total = 0.0
    map {
        total += it.precio * it.cantidad
    }
    return SIMBOLO_SOLES + "%.2f".format(total)
}