package com.akipa.ui.gestion_pedidos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akipa.databinding.FragmentGestionPedidosBinding

class GestionPedidosFragment : Fragment() {

    private lateinit var binding: FragmentGestionPedidosBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGestionPedidosBinding.inflate(inflater, container, false)

        return binding.root
    }
}