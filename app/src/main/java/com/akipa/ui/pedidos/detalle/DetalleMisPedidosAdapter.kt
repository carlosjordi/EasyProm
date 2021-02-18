package com.akipa.ui.pedidos.detalle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akipa.databinding.ItemDetalleMisPedidosBinding
import com.akipa.dto.Detalle

class DetalleMisPedidosAdapter :
    ListAdapter<Detalle, DetalleMisPedidosAdapter.DetallePedidoViewHolder>(DetalleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetallePedidoViewHolder =
        DetallePedidoViewHolder.from(parent)

    override fun onBindViewHolder(holder: DetallePedidoViewHolder, position: Int) {
        val detalle = getItem(position)
        holder.bind(detalle)
    }

    class DetallePedidoViewHolder(private val binding: ItemDetalleMisPedidosBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(detalle: Detalle) {
            binding.detalle = detalle
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): DetallePedidoViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemDetalleMisPedidosBinding.inflate(inflater, parent, false)
                return DetallePedidoViewHolder(binding)
            }
        }
    }
}

class DetalleDiffCallback : DiffUtil.ItemCallback<Detalle>() {
    override fun areItemsTheSame(oldItem: Detalle, newItem: Detalle): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Detalle, newItem: Detalle): Boolean =
        oldItem.id == newItem.id
}