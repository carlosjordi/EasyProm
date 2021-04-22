package com.akipa.ui.mantenimiento_platos.agregar_plato

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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.akipa.databinding.FragmentAgregarPlatoBinding
import com.akipa.dto.request.RegistrarPlatoRequest
import com.akipa.utils.*
import java.io.ByteArrayOutputStream

const val IMAGEN_PLATO_REQUEST_CODE = 777

const val CAMERA_PERMISSION_CODE = 17
const val GALLERY_PERMISSION_CODE = 27

const val CAMERA_REQUEST_CODE = 37
const val GALLERY_REQUEST_CODE = 47

class AgregarPlatoFragment : Fragment() {

    private lateinit var binding: FragmentAgregarPlatoBinding
    private val viewModel: AgregarPlatoViewModel by viewModels()
    private lateinit var fotoBitmap: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAgregarPlatoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.platoImagen.setOnClickListener { seleccionarImagenParaPlato() }
        binding.agregarPlatoBoton.setOnClickListener { agregarPlato() }

        viewModel.platoRegistradoResultado.observe(viewLifecycleOwner) { resultado ->
            if (resultado == Constantes.PLATO_REGISTRADO_MENSAJE_EXITOSO) {
                findNavController().navigate(AgregarPlatoFragmentDirections.actionAgregarPlatoFragmentToListaProductosFragment())
                viewModel.registrarPlatoTerminado()
            }
        }

        return binding.root
    }

    // TODO: validaciones pendientes
    private fun agregarPlato() {
        val nombre = binding.nombrePlatoInput.text.toString()
        val precio = binding.precioPlatoInput.text.toString().toDouble()
        val descripcion = binding.descripcionPlatoInput.text.toString()
        val foto = imagenToString(fotoBitmap)

        val plato = RegistrarPlatoRequest(nombre, precio, descripcion, foto)
        viewModel.registrarPlato(plato)
    }

    private fun seleccionarImagenParaPlato() {
        val opciones = arrayOf(
            IMAGEN_PLATO_OPCION_CAMARA,
            IMAGEN_PLATO_OPCION_GALERIA,
            IMAGEN_PLATO_OPCION_CANCELAR
        )
        AlertDialog.Builder(activity).apply {
            setTitle(IMAGEN_PLATO_TITULO)
            setItems(opciones) { dialog: DialogInterface, which: Int ->
                when (opciones[which]) {
                    IMAGEN_PLATO_OPCION_CAMARA -> solicitarPermiso(CAMERA_PERMISSION_CODE)
                    IMAGEN_PLATO_OPCION_GALERIA -> solicitarPermiso(GALLERY_PERMISSION_CODE)
                    IMAGEN_PLATO_OPCION_CANCELAR -> dialog.dismiss()
                }
            }
        }.show()
    }

    /**
     * Devuelve verdadero si tenemos permiso para usar la camara.
     * Falso en caso contrario
     */
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

    private fun solicitarPermiso(cual: Int) {
        when (cual) {
            CAMERA_PERMISSION_CODE -> {
                if (!tienePermisoCamara()) {
                    requestPermissions(
                        arrayOf(Manifest.permission.CAMERA),
                        IMAGEN_PLATO_REQUEST_CODE
                    )
                } else {
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
                        startActivityForResult(it, CAMERA_REQUEST_CODE)
                    }
                }
            }
            GALLERY_PERMISSION_CODE -> {
                if (!tienePermisoGaleria()) {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        IMAGEN_PLATO_REQUEST_CODE
                    )
                } else {
                    Intent(Intent.ACTION_PICK).also {
                        it.type = "image/*"
                        startActivityForResult(it, GALLERY_REQUEST_CODE)
                    }
                }
            }
        }
    }

    private fun imagenToString(bitmap: Bitmap): String {
        ByteArrayOutputStream().also {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, it)
            val bytes = it.toByteArray()
            return Base64.encodeToString(bytes, Base64.DEFAULT)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == IMAGEN_PLATO_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (permissions[0]) {
                Manifest.permission.CAMERA -> {
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
                        startActivityForResult(it, CAMERA_REQUEST_CODE)
                    }
                }
                Manifest.permission.READ_EXTERNAL_STORAGE -> {
                    Intent(Intent.ACTION_PICK).also {
                        it.type = "image/*"
                        startActivityForResult(it, GALLERY_REQUEST_CODE)
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && data != null) {
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
    }

    private fun setearImagenDesdeGaleria(data: Intent) {
        val input = data.data?.let { requireActivity().contentResolver.openInputStream(it) }
        fotoBitmap = BitmapFactory.decodeStream(input)
        binding.platoImagen.setImageBitmap(fotoBitmap)
    }
}