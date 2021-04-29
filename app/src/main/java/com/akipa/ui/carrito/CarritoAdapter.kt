package com.akipa.ui.carrito

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akipa.database.plato_en_carrito.PlatoEnCarrito
import com.akipa.databinding.ItemPlatoEnCarritoBinding

class CarritoAdapter(private val clickListener: OnClickItemsCarrito) :
    ListAdapter<PlatoEnCarrito, CarritoAdapter.CarritoViewHolder>(PlatoEnCarritoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder =
        CarritoViewHolder.from(parent)

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val platoEnCarrito = getItem(position)
        holder.bind(platoEnCarrito, clickListener)
    }

    class CarritoViewHolder(private val binding: ItemPlatoEnCarritoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(platoEnCarrito: PlatoEnCarrito, clickListener: OnClickItemsCarrito) {
            binding.platoEnCarrito = platoEnCarrito
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): CarritoViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemPlatoEnCarritoBinding.inflate(inflater, parent, false)
                return CarritoViewHolder(binding)
            }
        }
    }
}

class PlatoEnCarritoDiffCallback : DiffUtil.ItemCallback<PlatoEnCarrito>() {

    override fun areItemsTheSame(oldItem: PlatoEnCarrito, newItem: PlatoEnCarrito): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: PlatoEnCarrito, newItem: PlatoEnCarrito): Boolean =
        oldItem == newItem
}

/**
 * Interface que se encargará de manejar
 * los 2 eventos de click en cada plato del carrito
 */
interface OnClickItemsCarrito {

    fun onAgregarClick(platoEnCarrito: PlatoEnCarrito)
    fun onReducirClick(platoEnCarrito: PlatoEnCarrito)
    fun onEliminarClick(platoEnCarrito: PlatoEnCarrito)
}