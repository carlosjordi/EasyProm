package com.akipa.ui.pedidos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akipa.databinding.FragmentMisPedidosBinding

class MisPedidosFragment : Fragment() {

    private lateinit var binding: FragmentMisPedidosBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMisPedidosBinding.inflate(inflater, container, false)

        return binding.root
    }
}