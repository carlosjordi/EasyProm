package com.akipa.ui.gestion_pedidos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.akipa.R
import com.akipa.databinding.FragmentGestionPedidosBinding
import com.akipa.dto.request.GestionarPedidoRequest
import com.akipa.entidades.CabeceraPedido
import com.akipa.entidades.toCabeceraPedido
import com.akipa.ui.mantenimiento_platos.gestion_platos.GestionPlatoDialog
import com.akipa.ui.pedidos.detalle.DetalleMisPedidosAdapter
import com.akipa.utils.Constantes

class GestionPedidosFragment : Fragment() {

    private lateinit var binding: FragmentGestionPedidosBinding
    private val viewModel: GestionPedidosViewModel by viewModels()
    private var idPedido = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGestionPedidosBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        val adapter = GestionPedidosAdapter(GestionPedidosListener {
            idPedido = it.id
            viewModel.solicitarDetallePedido(idPedido)
            viewModel.pedidoSeleccionado(it.toCabeceraPedido())
            activarCajaComentarioYBotonesUI()
        })
        binding.listaPedidos?.adapter = adapter

        viewModel.listaCabeceraPedidos.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.cabeceraPedido.observe(viewLifecycleOwner) {
            actualizarUI(it)
        }

        val detalleAdapter = DetalleMisPedidosAdapter()
        binding.listaDetallePedido?.adapter = detalleAdapter

        viewModel.listaDetallePedido.observe(viewLifecycleOwner) {
            detalleAdapter.submitList(it)
        }
        viewModel.costeTotal.observe(viewLifecycleOwner) {
            binding.costeTotalText?.text =
                getString(R.string.gestion_pedido_coste_total, viewModel.costeTotal.value)
        }

        binding.aceptarPedidoBoton?.setOnClickListener { aceptarPedido() }
        binding.rechazarPedidoBoton?.setOnClickListener { rechazarPedido() }

        return binding.root
    }

    private fun rechazarPedido() {
        val request = GestionarPedidoRequest(
            idPedido,
            binding.observacionInput?.text.toString(),
            Constantes.ESTADO_PEDIDO_RECHAZADO
        )
        GestionPlatoDialog(
            message = getString(R.string.gestion_pedido_confirmar_negativo),
            onPositiveClick = {
                viewModel.gestionarPedido(request)
            }).show(childFragmentManager, "Confirmar Rechazo")
    }

    private fun aceptarPedido() {
        val request = GestionarPedidoRequest(
            idPedido,
            binding.observacionInput?.text.toString(),
            Constantes.ESTADO_PEDIDO_ACEPTADO
        )
        GestionPlatoDialog(
            message = getString(R.string.gestion_pedido_confirmar_positivo),
            onPositiveClick = {
                viewModel.gestionarPedido(request)
            }).show(childFragmentManager, "Confirmar Acepto")
    }

    private fun activarCajaComentarioYBotonesUI() {
        binding.tilObservacion?.isVisible = true
        binding.aceptarPedidoBoton?.isVisible = true
        binding.rechazarPedidoBoton?.isVisible = true
    }

    private fun actualizarUI(pedido: CabeceraPedido) {
        tipoPedidoTitulo(pedido)
        localTitulo(pedido)
        fechaDireccionTitulo(pedido)
        horaReferenciaTitulo(pedido)
        estadoTitulo(pedido)
    }

    private fun tipoPedidoTitulo(pedido: CabeceraPedido) {
        when (pedido.tipo_pedido) {
            Constantes.TIPO_PEDIDO_RECOJO_EN_TIENDA -> binding.tipoPedidoTitulo?.text =
                context?.getString(
                    R.string.recojo_en_tienda
                )
            Constantes.TIPO_PEDIDO_DELIVERY -> binding.tipoPedidoTitulo?.text =
                context?.getString(
                    R.string.delivery
                )
        }
    }

    private fun localTitulo(pedido: CabeceraPedido) {
        when (pedido.tipo_pedido) {
            Constantes.TIPO_PEDIDO_RECOJO_EN_TIENDA -> {
                binding.localPedido?.isVisible = true
                binding.localPedido?.text =
                    requireContext().getString(R.string.gestion_pedido_local, pedido.local)
            }
            Constantes.TIPO_PEDIDO_DELIVERY -> binding.localPedido?.isVisible = false
        }
    }

    private fun fechaDireccionTitulo(pedido: CabeceraPedido) {
        when (pedido.tipo_pedido) {
            Constantes.TIPO_PEDIDO_RECOJO_EN_TIENDA -> binding.fechaDireccionPedido?.text =
                requireContext().getString(R.string.gestion_pedido_fecha, pedido.fecha)
            Constantes.TIPO_PEDIDO_DELIVERY -> binding.fechaDireccionPedido?.text =
                requireContext().getString(
                    R.string.gestion_pedido_direccion,
                    pedido.direccion
                )
        }
    }

    private fun horaReferenciaTitulo(pedido: CabeceraPedido) {
        when (pedido.tipo_pedido) {
            Constantes.TIPO_PEDIDO_RECOJO_EN_TIENDA -> binding.horaReferenciaPedido?.text =
                requireContext().getString(R.string.gestion_pedido_hora, pedido.hora)
            Constantes.TIPO_PEDIDO_DELIVERY -> binding.horaReferenciaPedido?.text =
                requireContext().getString(
                    R.string.gestion_pedido_referencia,
                    pedido.referencia
                )
        }
    }

    private fun estadoTitulo(pedido: CabeceraPedido) {
        binding.estadoPedido?.text = when (pedido.estado) {
            Constantes.ESTADO_PEDIDO_EN_ESPERA -> requireContext().getString(
                R.string.gestion_pedido_estado,
                requireContext().getString(R.string.estado_pedido_en_espera)
            )
            Constantes.ESTADO_PEDIDO_ACEPTADO -> requireContext().getString(
                R.string.gestion_pedido_estado,
                requireContext().getString(R.string.estado_pedido_aceptado)
            )
            Constantes.ESTADO_PEDIDO_RECHAZADO -> requireContext().getString(
                R.string.gestion_pedido_estado,
                requireContext().getString(R.string.estado_pedido_rechazado)
            )
            else -> ""
        }
    }
}