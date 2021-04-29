package com.akipa.ui.mantenimiento_platos.gestion_platos.actualizar_plato

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.akipa.R
import com.akipa.databinding.FragmentActualizarPlatoBinding
import com.akipa.dto.request.ActualizarPlatoRequest
import com.akipa.ui.mantenimiento_platos.agregar_plato.*
import com.akipa.ui.mantenimiento_platos.gestion_platos.GestionPlatoDialog
import com.akipa.utils.*
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
        binding.platoImagen.setOnClickListener { seleccionarImagenParaPlato() }

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

        GestionPlatoDialog(
            message = getString(R.string.gestion_plato_actualizar_dialog_message),
            onPositiveClick = {
                viewModel.actualizarPlato(plato)
            }).show(childFragmentManager, "Actualizar Plato")
    }

    private fun imagenToString(bitmap: Bitmap): String {
        ByteArrayOutputStream().also {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, it)
            val bytes = it.toByteArray()
            return Base64.encodeToString(bytes, Base64.DEFAULT)
        }
    }

    private fun seleccionarImagenParaPlato() {
        val opciones = arrayOf(
            IMAGEN_PLATO_OPCION_CAMARA,
            IMAGEN_PLATO_OPCION_GALERIA,
            IMAGEN_PLATO_OPCION_CANCELAR
        )
        mostrarOpcionesParaImagenPlato(opciones)
    }

    private fun mostrarOpcionesParaImagenPlato(opciones: Array<String>) {
        AlertDialog.Builder(activity).apply {
            setTitle(IMAGEN_PLATO_TITULO_ACTUALIZAR)
            setItems(opciones) { dialog: DialogInterface, which: Int ->
                when (opciones[which]) {
                    IMAGEN_PLATO_OPCION_CAMARA -> solicitarPermiso(CAMERA_PERMISSION_CODE)
                    IMAGEN_PLATO_OPCION_GALERIA -> solicitarPermiso(GALLERY_PERMISSION_CODE)
                    IMAGEN_PLATO_OPCION_CANCELAR -> dialog.dismiss()
                }
            }
        }.show()
    }

    private fun solicitarPermiso(cual: Int) {
        when (cual) {
            CAMERA_PERMISSION_CODE -> {
                if (tienePermisoCamara()) {
                    abrirCamara()
                    return
                }
                pedirPermisoCamara()
            }
            GALLERY_PERMISSION_CODE -> {
                if (tienePermisoGaleria()) {
                    abrirGaleria()
                    return
                }
                pedirPermisoGaleria()
            }
        }
    }

    private fun abrirCamara() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            startActivityForResult(it, CAMERA_REQUEST_CODE)
        }
    }

    private fun abrirGaleria() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            startActivityForResult(it, GALLERY_REQUEST_CODE)
        }
    }

    private fun pedirPermisoCamara() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            IMAGEN_PLATO_REQUEST_CODE
        )
    }

    private fun pedirPermisoGaleria() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            IMAGEN_PLATO_REQUEST_CODE
        )
    }

    private fun tienePermisoCamara(): Boolean =
        ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    private fun tienePermisoGaleria(): Boolean =
        ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == IMAGEN_PLATO_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (permissions[0]) {
                Manifest.permission.CAMERA -> abrirCamara()
                Manifest.permission.READ_EXTERNAL_STORAGE -> abrirGaleria()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null && resultCode == RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> setearImagenDesdeCamara(data)
                GALLERY_REQUEST_CODE -> setearImagenDesdeGaleria(data)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setearImagenDesdeCamara(data: Intent) {
        fotoBitmap = data.extras?.get("data") as Bitmap
        binding.platoImagen.setImageBitmap(fotoBitmap)
        imagenFueCambiada = true
    }

    private fun setearImagenDesdeGaleria(data: Intent) {
        val input = data.data?.let { requireActivity().contentResolver.openInputStream(it) }
        fotoBitmap = BitmapFactory.decodeStream(input)
        binding.platoImagen.setImageBitmap(fotoBitmap)
        imagenFueCambiada = true
    }
}