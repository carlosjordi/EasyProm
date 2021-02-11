package com.akipa.ui.mantenimiento_platos.gestion_platos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.akipa.databinding.FragmentGestionPlatosBinding
import com.akipa.entidades.Plato
import com.akipa.ui.lista_platos.ListaProductosViewModel

class GestionPlatosFragment : Fragment(), GestionPlatosListener {

    private lateinit var binding: FragmentGestionPlatosBinding
    private val viewModel: ListaProductosViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentGestionPlatosBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = GestionPlatosAdapter(this)
        binding.gestionPlatosListado.adapter = adapter

        return binding.root
    }

    override fun onEditarClick(plato: Plato) {
        Log.i("Gestion Platos", "Editar: $plato")
    }

    override fun onEliminarClick(idPlato: Int) {
        Log.i("Gestion Platos", "Eliminar: $idPlato")
    }
}