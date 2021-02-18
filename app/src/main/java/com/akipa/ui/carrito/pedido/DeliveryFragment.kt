package com.akipa.ui.carrito.pedido

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.akipa.databinding.FragmentDeliveryBinding
import com.akipa.dto.request.DeliveryRequest
import com.akipa.ui.carrito.CarritoFragmentDirections
import com.akipa.utils.UniqueIdentifier

class DeliveryFragment : Fragment() {

    private lateinit var binding: FragmentDeliveryBinding
    private val viewModel: PedidosViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeliveryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.realizarPedidoBoton.setOnClickListener { realizarPedido() }
        viewModel.platos.observe(viewLifecycleOwner) {
            viewModel.asignarPlatosDelCarrito()
        }

        viewModel.seTerminoOperacionCarrito.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(CarritoFragmentDirections.actionCarritoFragmentToMisPedidosFragment())
                viewModel.operacionCarritoTerminada()
            }
        }

        return binding.root
    }

    private fun realizarPedido() {
        val idSolicitante = UniqueIdentifier.getUniqueIdentifier()
        val direccion = binding.direccionInput.text.toString()
        val referencia = binding.referenciaInput.text.toString()
        val pedido = DeliveryRequest(idSolicitante, direccion, referencia)
        viewModel.realizarPedidoTipoDelivery(pedido)
    }
}