package com.akipa.ui.pedidos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akipa.databinding.ItemMisPedidosBinding
import com.akipa.dto.SolicitarCabecerasResponseItem

class MisPedidosAdapter(private val listener: PedidosListener) :
    ListAdapter<SolicitarCabecerasResponseItem, MisPedidosAdapter.MisPedidosViewHolder>(
        PedidosDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MisPedidosViewHolder =
        MisPedidosViewHolder.from(parent)

    override fun onBindViewHolder(holder: MisPedidosViewHolder, position: Int) {
        val pedido = getItem(position)
        holder.bind(pedido, listener)
    }

    class MisPedidosViewHolder(private val binding: ItemMisPedidosBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pedido: SolicitarCabecerasResponseItem, clickListener: PedidosListener) {
            binding.pedido = pedido
            binding.listener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MisPedidosViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemMisPedidosBinding.inflate(inflater, parent, false)
                return MisPedidosViewHolder(binding)
            }
        }
    }
}

class PedidosDiffCallback : DiffUtil.ItemCallback<SolicitarCabecerasResponseItem>() {
    override fun areItemsTheSame(
        oldItem: SolicitarCabecerasResponseItem,
        newItem: SolicitarCabecerasResponseItem
    ): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(
        oldItem: SolicitarCabecerasResponseItem,
        newItem: SolicitarCabecerasResponseItem
    ): Boolean =
        oldItem.id == newItem.id
}

class PedidosListener(val clickListener: (pedido: SolicitarCabecerasResponseItem) -> Unit) {
    fun onClick(pedido: SolicitarCabecerasResponseItem) = clickListener(pedido)
}