package com.akipa.utils

import com.akipa.database.personal_logueado.PersonalLogueado
import com.akipa.network.BASE_URL

object Constantes {

    const val BASE_PARA_FOTOS = "${BASE_URL}imagenes_platos/"

    const val CANTIDAD_PLATOS_MINIMA = 1
    const val CANTIDAD_PLATOS_MAXIMA = 10
    const val PLATO_REGISTRADO_MENSAJE_EXITOSO = "Plato Registrado"
    const val PLATO_ACTUALIZADO_MENSAJE_EXITOSO = "Plato Actualizado"
    const val PLATO_ELIMINADO_MENSAJE_EXITOSO = "Plato Eliminado"
    const val IMAGEN_NO_ACTUALIZADA = "Imagen no fue actualizada"

    const val PEDIDO_REGISTRADO_MENSAJE_EXITOSO = "Pedido Registrado"
    const val TIPO_PEDIDO_RECOJO_EN_TIENDA = 1
    const val TIPO_PEDIDO_DELIVERY = 2
    const val ESTADO_PEDIDO_EN_ESPERA = 0
    const val ESTADO_PEDIDO_ACEPTADO = 1
    const val ESTADO_PEDIDO_RECHAZADO = 2

    const val PUESTO_PERSONAL_ADMIN = "Admin"
    const val PUESTO_PERSONAL_CAJERO = "Cajero"

    /**
     * Mantendrá info del personal logueado actualmente.
     * Si es que su valor es nulo significa que no hay sesión
     * iniciada.
     */
    var personalAkipaLogueado: PersonalLogueado? = null
}