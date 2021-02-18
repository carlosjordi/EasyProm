package com.akipa.ui.pedidos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.akipa.databinding.FragmentMisPedidosBinding
import com.akipa.entidades.toCabeceraPedido

class MisPedidosFragment : Fragment() {

    private lateinit var binding: FragmentMisPedidosBinding
    private val viewModel: MisPedidosViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMisPedidosBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = MisPedidosAdapter(PedidosListener {
            findNavController().navigate(
                MisPedidosFragmentDirections.actionMisPedidosFragmentToDetalleMisPedidosFragment(
                    it.toCabeceraPedido()
                )
            )
        })
        binding.listaMisPedidos.adapter = adapter

        viewModel.listaPedidos.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }
}