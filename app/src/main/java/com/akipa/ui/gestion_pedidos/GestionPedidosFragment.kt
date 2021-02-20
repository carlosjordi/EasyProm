package com.akipa.ui.gestion_pedidos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.akipa.databinding.FragmentGestionPedidosBinding

class GestionPedidosFragment : Fragment() {

    private lateinit var binding: FragmentGestionPedidosBinding
    private val viewModel: GestionPedidosViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGestionPedidosBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        val adapter = GestionPedidosAdapter(GestionPedidosListener {
            // evento de click
        })
        binding.listaPedidos?.adapter = adapter

        viewModel.listaCabeceraPedidos.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }
}