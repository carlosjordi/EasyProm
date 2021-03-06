package com.akipa.ui.inicio_sesion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.akipa.database.AkipaLocalDatabase
import com.akipa.databinding.FragmentInicioSesionBinding
import com.akipa.network.AkipaAPI
import com.akipa.utils.Constantes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InicioSesionFragment : Fragment() {

    private lateinit var binding: FragmentInicioSesionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInicioSesionBinding.inflate(inflater, container, false)

        binding.iniciarSesionBoton.setOnClickListener { iniciarSesion() }

        return binding.root
    }

    // TODO: validaciones pendientes
    private fun iniciarSesion() = lifecycleScope.launch(Dispatchers.IO) {
        val usuario = binding.usuarioInicioSesionInput.text.toString()
        val password = binding.passwordInicioSesionInput.text.toString()

        val personal = AkipaAPI.retrofitService.iniciarSesionAsync(usuario, password).await()
        // si el personal está autorizado a loguearse, procedemos a salvar la sesión localmente
        // y navegamos al listado de platos
        if (personal.id != null) {
            AkipaLocalDatabase.getInstance(requireContext().applicationContext)
                .personalDao
                .iniciarSesion(personal.toPersonalLogueado())

            // Se guarda la sesión en esta variable global
            Constantes.personalAkipaLogueado =
                AkipaLocalDatabase.getInstance(requireContext().applicationContext)
                    .personalDao
                    .obtenerPersonalLogueado()

            withContext(Dispatchers.Main) {
                // se prepara para el cambio del toolbar menu
                activity?.invalidateOptionsMenu()
                findNavController()
                    .navigate(InicioSesionFragmentDirections.actionInicioSesionFragmentToListaProductosFragment())
            }
        }
    }
}