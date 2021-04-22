package com.akipa.ui.mantenimiento_platos.gestion_platos.actualizar_plato

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.akipa.databinding.FragmentActualizarPlatoBinding
import com.akipa.dto.request.ActualizarPlatoRequest
import com.akipa.ui.mantenimiento_platos.agregar_plato.CAMERA_PERMISSION_CODE
import com.akipa.ui.mantenimiento_platos.agregar_plato.IMAGEN_PLATO_REQUEST_CODE
import com.akipa.utils.Constantes
import java.io.ByteArrayOutputStream

class ActualizarPlatoFragment : Fragment() {

    private lateinit var binding: FragmentActualizarPlatoBinding
    private val viewModel: ActualizarPlatoViewModel by viewModels()
    private val args: ActualizarPlatoFragmentArgs by navArgs()
    private lateinit var fotoBitmap: Bitmap
    private var imagenFueCambiada = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActualizarPlatoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.plato = args.plato
        binding.editarPlatoBoton.setOnClickListener { actualizarPlato() }
        binding.platoImagen.setOnClickListener { solicitarPermisoCamara() }

        viewModel.platoActualizadoResultado.observe(viewLifecycleOwner) {
            if (it == Constantes.PLATO_ACTUALIZADO_MENSAJE_EXITOSO) {
                findNavController().navigate(ActualizarPlatoFragmentDirections.actionActualizarPlatoFragmentToGestionPlatosFragment())
                viewModel.actualizarPlatoTerminado()
            }
        }
        return binding.root
    }

    // TODO: Validaciones pendientes
    private fun actualizarPlato() {
        val id = args.plato.id
        val nombre = binding.nombrePlatoInput.text.toString()
        val precio = binding.precioPlatoInput.text.toString().toDouble()
        val descripcion = binding.descripcionPlatoInput.text.toString()
        val foto =
            if (imagenFueCambiada) imagenToString(fotoBitmap) else Constantes.IMAGEN_NO_ACTUALIZADA
        val plato = ActualizarPlatoRequest(id, nombre, precio, descripcion, foto)
        viewModel.actualizarPlato(plato)
    }

    private fun imagenToString(bitmap: Bitmap): String {
        ByteArrayOutputStream().also {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, it)
            val bytes = it.toByteArray()
            return Base64.encodeToString(bytes, Base64.DEFAULT)
        }
    }

    private fun solicitarPermisoCamara() {
        if (!tienePermisoCamara()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        } else {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
                startActivityForResult(it, IMAGEN_PLATO_REQUEST_CODE)
            }
        }
    }

    private fun tienePermisoCamara(): Boolean =
        ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
                startActivityForResult(it, IMAGEN_PLATO_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IMAGEN_PLATO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            fotoBitmap = data?.extras?.get("data") as Bitmap
            binding.platoImagen.setImageBitmap(fotoBitmap)
            imagenFueCambiada = true
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}