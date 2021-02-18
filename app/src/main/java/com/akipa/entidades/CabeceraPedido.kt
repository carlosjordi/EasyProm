package com.akipa.entidades

import android.os.Parcelable
import com.akipa.dto.SolicitarCabecerasResponseItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class CabeceraPedido(
    val id: Int,
    val direccion: String,
    val referencia: String,
    val fecha: String,
    val hora: String,
    val local: String,
    val observacion: String?,
    val estado: Int, // 0 en espera | 1 aceptado | 2 rechazado
    val tipo_pedido: Int // 1 recojo en tienda | 2 delivery
) : Parcelable

fun SolicitarCabecerasResponseItem.toCabeceraPedido(): CabeceraPedido =
    CabeceraPedido(
        id,
        direccion,
        referencia,
        fecha,
        hora,
        local,
        observacion,
        estado,
        tipo_pedido
    )