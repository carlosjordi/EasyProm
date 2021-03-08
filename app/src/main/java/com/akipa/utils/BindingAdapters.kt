package com.akipa.utils

import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.akipa.R
import com.akipa.database.plato_en_carrito.PlatoEnCarrito
import com.akipa.dto.SolicitarCabecerasResponseItem
import com.akipa.entidades.CabeceraPedido
import com.akipa.entidades.Plato
import com.akipa.ui.carrito.CarritoAdapter
import com.akipa.ui.lista_platos.EstadoListadoPlatos
import com.akipa.ui.lista_platos.ListaPlatosAdapter
import com.akipa.ui.mantenimiento_platos.gestion_platos.GestionPlatosAdapter

/**
 * Utilizado para indicarle la lista de platos a mostrar
 * en la pantalla principal de la app
 */
@BindingAdapter("listaPlatos")
fun RecyclerView.bindRecyclerView(data: List<Plato>?) {
    val adapter = this.adapter as ListaPlatosAdapter?
    adapter?.submitList(data)
}

@BindingAdapter("listaGestionPlatos")
fun RecyclerView.bindGestionPlatos(data: List<Plato>?) {
    val adapter = this.adapter as GestionPlatosAdapter?
    adapter?.submitList(data)
}

/**
 * Utilizado para indicarle la lista de platos agregados
 * al carrito
 */
@BindingAdapter("listaPlatosEnCarrito")
fun RecyclerView.bindPlatosEnCarrito(data: List<PlatoEnCarrito>?) {
    val adapter = this.adapter as CarritoAdapter?
    adapter?.submitList(data)
}

/**
 * Utilizado para mostrar imagenes. Solo requiere que se le
 * pase la URL de la misma
 */
@BindingAdapter("url_imagen")
fun ImageView.bingImage(imgUrl: String?) {
    imgUrl?.let { url ->
        val imgUri = url.toUri().buildUpon().scheme("https").build()
        Glide.with(this.context)
            .load(imgUri)
            .centerCrop()
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(this)
    } ?: run {
        Glide.with(this.context)
            .load(R.drawable.ic_broken_image)
            .centerCrop()
            .into(this)
    }
}

/**
 * Se encargará de renderizar distintas imagenes en base
 * al estado actual de los platos
 */
@BindingAdapter("estado_platos")
fun ImageView.statusPlatos(estado: EstadoListadoPlatos?) {
    when (estado) {
        EstadoListadoPlatos.CARGANDO -> {
            visibility = View.VISIBLE
            setImageResource(R.drawable.loading_animation)
        }
        EstadoListadoPlatos.ERROR -> {
            visibility = View.VISIBLE
            setImageResource(R.drawable.ic_connection_error)
        }
        EstadoListadoPlatos.LISTO -> visibility = View.GONE
    }
}

@BindingAdapter("tipo_pedido")
fun TextView.tipoPedido(tipoPedido: Int?) {
    tipoPedido?.let {
        if (tipoPedido == Constantes.TIPO_PEDIDO_RECOJO_EN_TIENDA) {
            text = context.getString(R.string.recojo_en_tienda)
        } else if (tipoPedido == Constantes.TIPO_PEDIDO_DELIVERY) {
            text = context.getString(R.string.delivery)
        }
    }
}

@BindingAdapter("estado_pedido")
fun TextView.estadoPedido(estadoPedido: Int?) {
    estadoPedido?.let {
        when (it) {
            Constantes.ESTADO_PEDIDO_EN_ESPERA -> text =
                context.getString(R.string.estado_pedido_en_espera)
            Constantes.ESTADO_PEDIDO_ACEPTADO -> text =
                context.getString(R.string.estado_pedido_aceptado)
            Constantes.ESTADO_PEDIDO_RECHAZADO -> text =
                context.getString(R.string.estado_pedido_rechazado)
        }
    }
}

@BindingAdapter("direccion_fecha", "pedido")
fun TextView.direccionFechaBinding(estadoPedido: Int?, pedido: SolicitarCabecerasResponseItem?) {
    estadoPedido?.let {
        when (it) {
            Constantes.TIPO_PEDIDO_RECOJO_EN_TIENDA -> text =
                context.getString(R.string.fecha_display, pedido?.fecha)
            Constantes.TIPO_PEDIDO_DELIVERY -> text =
                context.getString(R.string.direccion_display, pedido?.direccion)
        }
    }
}

@BindingAdapter("referencia_hora", "pedido")
fun TextView.referenciaHoraBinding(estadoPedido: Int?, pedido: SolicitarCabecerasResponseItem?) {
    estadoPedido?.let {
        when (it) {
            Constantes.TIPO_PEDIDO_RECOJO_EN_TIENDA -> text =
                context.getString(R.string.hora_display, pedido?.hora)
            Constantes.TIPO_PEDIDO_DELIVERY -> pedido?.let { p ->
                if (p.referencia.isNotBlank()) text =
                    context.getString(R.string.referencia_display, p.referencia)
                else visibility = View.GONE
            }
            else -> text = ""
        }
    }
}

@BindingAdapter("delivery_label")
fun TextView.deliveryLabelDetallePedido(cabecera: CabeceraPedido?) {
    cabecera?.let {
        when (it.tipo_pedido) {
            Constantes.TIPO_PEDIDO_RECOJO_EN_TIENDA -> visibility = View.GONE
            Constantes.TIPO_PEDIDO_DELIVERY -> visibility = View.VISIBLE
        }
    }
}

@BindingAdapter("direccion_text")
fun TextView.direccionDetallePedido(cabecera: CabeceraPedido?) {
    cabecera?.let {
        when (it.tipo_pedido) {
            Constantes.TIPO_PEDIDO_RECOJO_EN_TIENDA -> visibility = View.GONE
            Constantes.TIPO_PEDIDO_DELIVERY -> text = cabecera.direccion
        }
    }
}

@BindingAdapter("referencia_text")
fun TextView.referenciaDetallePedido(cabecera: CabeceraPedido?) {
    cabecera?.let {
        when (it.tipo_pedido) {
            Constantes.TIPO_PEDIDO_RECOJO_EN_TIENDA -> visibility = View.GONE
            Constantes.TIPO_PEDIDO_DELIVERY -> text = cabecera.referencia
        }
    }
}

@BindingAdapter("recojo_tienda_label")
fun TextView.recojoTiendaLabelDetallePedido(cabecera: CabeceraPedido?) {
    cabecera?.let {
        when (it.tipo_pedido) {
            Constantes.TIPO_PEDIDO_RECOJO_EN_TIENDA -> visibility = View.VISIBLE
            Constantes.TIPO_PEDIDO_DELIVERY -> visibility = View.GONE
        }
    }
}

@BindingAdapter("fecha_text")
fun TextView.fechaDetallePedido(cabecera: CabeceraPedido?) {
    cabecera?.let {
        when (it.tipo_pedido) {
            Constantes.TIPO_PEDIDO_RECOJO_EN_TIENDA -> text = cabecera.fecha
            Constantes.TIPO_PEDIDO_DELIVERY -> visibility = View.GONE
        }
    }
}

@BindingAdapter("hora_text")
fun TextView.horaDetallePedido(cabecera: CabeceraPedido?) {
    cabecera?.let {
        when (it.tipo_pedido) {
            Constantes.TIPO_PEDIDO_RECOJO_EN_TIENDA -> text = cabecera.hora
            Constantes.TIPO_PEDIDO_DELIVERY -> visibility = View.GONE
        }
    }
}

@BindingAdapter("isGone")
fun View.bindIsGone(isGone: Boolean) {
    visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@BindingAdapter("estadoBackgroundColor")
fun CardView.bindBackgroundTint(pedido: SolicitarCabecerasResponseItem?) {
    when (pedido?.estado) {
        Constantes.ESTADO_PEDIDO_EN_ESPERA -> setCardBackgroundColor(context.getColor(R.color.estado_en_espera))
        Constantes.ESTADO_PEDIDO_RECHAZADO -> setCardBackgroundColor(context.getColor(R.color.estado_rechazado))
        Constantes.ESTADO_PEDIDO_ACEPTADO -> setCardBackgroundColor(context.getColor(R.color.estado_aceptado))
    }
}