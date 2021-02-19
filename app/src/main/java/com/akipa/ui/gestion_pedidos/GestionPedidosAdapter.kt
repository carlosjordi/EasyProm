package com.akipa.ui.gestion_pedidos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akipa.databinding.ItemGestionTodosPedidosBinding
import com.akipa.dto.SolicitarCabecerasResponseItem

class GestionPedidosAdapter(private val listener: GestionPedidosListener) :
    ListAdapter<SolicitarCabecerasResponseItem, GestionPedidosAdapter.GestionPedidoViewHolder>(
        GestionPedidosDiffcallback()
    ) {

    class GestionPedidoViewHolder(private val binding: ItemGestionTodosPedidosBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pedido: SolicitarCabecerasResponseItem, listener: GestionPedidosListener) {
            binding.pedido = pedido
            binding.listener = listener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): GestionPedidoViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemGestionTodosPedidosBinding.inflate(inflater, parent, false)
                return GestionPedidoViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GestionPedidoViewHolder =
        GestionPedidoViewHolder.from(parent)

    override fun onBindViewHolder(holder: GestionPedidoViewHolder, position: Int) {
        val pedido = getItem(position)
        holder.bind(pedido, listener)
    }
}

class GestionPedidosDiffcallback : DiffUtil.ItemCallback<SolicitarCabecerasResponseItem>() {
    override fun areItemsTheSame(
        oldItem: SolicitarCabecerasResponseItem,
        newItem: SolicitarCabecerasResponseItem
    ): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: SolicitarCabecerasResponseItem,
        newItem: SolicitarCabecerasResponseItem
    ): Boolean =
        oldItem == newItem
}

class GestionPedidosListener(private val listener: (pedido: SolicitarCabecerasResponseItem) -> Unit) {
    fun onClick(pedido: SolicitarCabecerasResponseItem) = listener(pedido)
}