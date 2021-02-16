package com.akipa.ui.carrito.pedido

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.akipa.R
import com.akipa.databinding.FragmentRecojoEnTiendaBinding
import com.akipa.dialogs.SeleccionadorFecha
import com.akipa.dialogs.SeleccionadorHora
import com.akipa.dto.request.RecojoEnTiendaRequest
import com.akipa.utils.UniqueIdentifier

class RecojoEnTiendaFragment : Fragment() {

    private lateinit var binding: FragmentRecojoEnTiendaBinding
    private val viewModel: PedidosViewModel by viewModels(factoryProducer = {
        PedidosViewModelFactory(requireActivity().application)
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecojoEnTiendaBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.startTimerTrigger.setOnClickListener { seleccionarHora() }
        binding.startDateTrigger.setOnClickListener { seleccionarFecha() }
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.akipa_locales,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.localSpinner.adapter = adapter
        }
        binding.realizarPedidoBoton.setOnClickListener { realizarPedido() }

        viewModel.platos.observe(viewLifecycleOwner) {
            viewModel.asignarPlatosDelCarrito()
        }

        return binding.root
    }

    private fun realizarPedido() {
        // le sumo 1 por que el spinner arranca en 0
        val idLocal = binding.localSpinner.selectedItemId.toInt() + 1
        val idSolicitante = UniqueIdentifier.getUniqueIdentifier()
        val fechaRecojo = binding.seleccionarFechaValor.text.toString()
        val horaRecojo = binding.seleccionarHoraValor.text.toString()

        val pedidoEnTienda =
            RecojoEnTiendaRequest(idLocal, idSolicitante, fechaRecojo, horaRecojo)
        viewModel.realizarPedidoTipoRecojoEnTienda(pedidoEnTienda)
    }

    private fun seleccionarFecha() {
        SeleccionadorFecha(binding).show(parentFragmentManager, "datePicker")
    }

    private fun seleccionarHora() {
        SeleccionadorHora(binding).show(parentFragmentManager, "timePicker")
    }
}