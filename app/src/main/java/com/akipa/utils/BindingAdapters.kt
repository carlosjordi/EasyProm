package com.akipa.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.akipa.R
import com.akipa.database.plato_en_carrito.PlatoEnCarrito
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