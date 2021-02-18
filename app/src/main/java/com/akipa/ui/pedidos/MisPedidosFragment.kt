package com.akipa.ui.pedidos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.akipa.databinding.FragmentMisPedidosBinding

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
            // cuando hagamos click
        })
        binding.listaMisPedidos.adapter = adapter

        viewModel.listaPedidos.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }
}