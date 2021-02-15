package com.akipa.database.plato_en_carrito

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.akipa.utils.formatearPrecio

@Entity(tableName = "platos_en_carrito")
data class PlatoEnCarrito(
    @PrimaryKey
    val id: Int,
    val nombre: String,
    val precio: Double,
    var cantidad: Int,
    val urlImagen: String?
) {
    @Ignore
    val precioFormateado = precio.formatearPrecio()

    @Ignore
    val cantidadUI = cantidad.toString()
}

fun List<PlatoEnCarrito>.obtenerCosteTotal(): Double {
    var total = 0.0
    map {
        total += it.precio * it.cantidad
    }
    return total
}