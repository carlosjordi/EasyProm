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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.akipa.R
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
    private var fotoBitmap: Bitmap? = null

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

    private fun agregarPlato() {
        val nombre = binding.nombrePlatoInput.text.toString()
        val precio = binding.precioPlatoInput.text.toString().toDoubleOrNull()
        val descripcion = binding.descripcionPlatoInput.text.toString()

        if (nombre.isBlank()) {
            Toast.makeText(
                context,
                getString(R.string.agregar_plato_mensaje_validacion_nombre),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (precio == null) {
            Toast.makeText(
                context,
                getString(R.string.agregar_plato_mensaje_validacion_precio),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (descripcion.isBlank()) {
            Toast.makeText(
                context,
                getString(R.string.agregar_plato_mensaje_validacion_descripcion),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (fotoBitmap == null) {
            Toast.makeText(
                context,
                getString(R.string.agregar_plato_mensaje_validacion_foto),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val foto = imagenToString(fotoBitmap!!)

        val plato = RegistrarPlatoRequest(nombre, precio, descripcion, foto)
        viewModel.registrarPlato(plato)
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
                Manifest.permission.CAMERA -> abrirCamara()
                Manifest.permission.READ_EXTERNAL_STORAGE -> abrirGaleria()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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