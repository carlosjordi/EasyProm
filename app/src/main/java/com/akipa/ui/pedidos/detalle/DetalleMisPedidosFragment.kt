package com.akipa.ui.pedidos.detalle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.akipa.databinding.FragmentDetalleMisPedidosBinding

class DetalleMisPedidosFragment : Fragment() {

    private lateinit var binding: FragmentDetalleMisPedidosBinding
    private val viewModel: DetalleMisPedidosViewModel by viewModels()
    private val args: DetalleMisPedidosFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetalleMisPedidosBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.cabecera = args.cabecera

        viewModel.solicitarDetalle(args.cabecera.id)

        return binding.root
    }
}