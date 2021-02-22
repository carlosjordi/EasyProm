package com.akipa.ui.carrito

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.akipa.R
import com.akipa.database.plato_en_carrito.PlatoEnCarrito
import com.akipa.databinding.FragmentCarritoBinding
import com.akipa.utils.PEDIDOS_DELIVERY
import com.akipa.utils.PEDIDOS_RECOJO_EN_TIENDA_INDEX
import com.akipa.utils.RecojosYDeliveryPageAdapter
import com.google.android.material.tabs.TabLayoutMediator

class CarritoFragment : Fragment(), OnClickItemsCarrito {

    private lateinit var binding: FragmentCarritoBinding
    private val viewModel: CarritoViewModel by viewModels(factoryProducer = {
        CarritoViewModel.CarritoViewModelFactory(application = requireActivity().application)
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCarritoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        val adapter = CarritoAdapter(this)
        binding.listaCarrito.adapter = adapter

        val tabs = binding.tabs
        val viewpager = binding.viewpager

        viewpager.adapter = RecojosYDeliveryPageAdapter(this)
        // Estamos manejando los tabs para recojo en tienda | delivery
        TabLayoutMediator(tabs, viewpager) { tab, position ->
            tab.text = obtenerTituloTab(position)
        }.attach()
        binding.irListadoBoton.setOnClickListener { navegarAListado() }

        return binding.root
    }

    override fun onAgregarClick(platoEnCarrito: PlatoEnCarrito) {
        viewModel.incrementarCantidadPlato(platoEnCarrito)
    }

    override fun onReducirClick(platoEnCarrito: PlatoEnCarrito) {
        viewModel.reducirCantidadPlato(platoEnCarrito)
    }

    override fun onEliminarClick(platoEnCarrito: PlatoEnCarrito) {
        viewModel.quitarPlato(platoEnCarrito)
    }

    private fun obtenerTituloTab(posicion: Int) =
        when (posicion) {
            PEDIDOS_RECOJO_EN_TIENDA_INDEX -> getString(R.string.tab_titulo_recojo_en_tienda)
            PEDIDOS_DELIVERY -> getString(R.string.tab_titulo_delivery)
            else -> throw IndexOutOfBoundsException()
        }

    private fun navegarAListado() {
        findNavController().navigate(CarritoFragmentDirections.actionCarritoFragmentToListaProductosFragment())
    }
}