package com.akipa.ui.mantenimiento_platos.gestion_platos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.akipa.R
import com.akipa.databinding.FragmentGestionPlatosBinding
import com.akipa.dto.request.EliminarPlatoRequest
import com.akipa.entidades.Plato
import com.akipa.ui.lista_platos.ListaPlatosViewModel

class GestionPlatosFragment : Fragment(), GestionPlatosListener {

    private lateinit var binding: FragmentGestionPlatosBinding
    private val viewModel: ListaPlatosViewModel by viewModels()

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
        findNavController().navigate(
            GestionPlatosFragmentDirections.actionGestionPlatosFragmentToActualizarPlatoFragment(
                plato
            )
        )
    }

    override fun onEliminarClick(idPlato: Int) {
        val plato = EliminarPlatoRequest(idPlato)
        GestionPlatoDialog(
            message = getString(R.string.gestion_plato_eliminar_dialog_message),
            onPositiveClick = {
                viewModel.eliminarPlato(plato)
            }).show(childFragmentManager, "Eliminar Plato")
    }
}