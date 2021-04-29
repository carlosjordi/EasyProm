package com.akipa.ui.detalle_plato

import android.app.Application
import androidx.lifecycle.*
import com.akipa.database.AkipaLocalDatabase
import com.akipa.database.plato_en_carrito.PlatoEnCarrito
import com.akipa.entidades.Plato
import com.akipa.utils.Constantes
import kotlinx.coroutines.*

class DetallePlatoViewModel(application: Application) : AndroidViewModel(application) {

    private val viewmodelJob = Job()
    private val coroutineScope = CoroutineScope(viewmodelJob + Dispatchers.Main)

    private val database = AkipaLocalDatabase.getInstance(application)

    /**
     * Variable a modificar internamente
     */
    private val _cantidadPlatos = MutableLiveData(1)

    /**
     * Variable a mostrar en la UI
     */
    val cantidadPlatos = Transformations.map(_cantidadPlatos) {
        it.toString()
    }

    private val _navegacionAlListadoPlatos = MutableLiveData(false)
    val navegacionAlListadoPlatos: LiveData<Boolean>
        get() = _navegacionAlListadoPlatos

    fun incrementarCantidadPlato() {
        _cantidadPlatos.value?.let { cantidad ->
            if (cantidad >= Constantes.CANTIDAD_PLATOS_MAXIMA)
                return@let
            _cantidadPlatos.value = _cantidadPlatos.value?.plus(1)
        }
    }

    fun reducirCantidadPlato() =
        _cantidadPlatos.value?.let { cantidad ->
            if (cantidad <= Constantes.CANTIDAD_PLATOS_MINIMA)
                return@let
            _cantidadPlatos.value = _cantidadPlatos.value?.minus(1)
        }

    fun agregarPlatoAlCarrito(plato: Plato) {
        coroutineScope.launch {
            val platoEnCarrito =
                PlatoEnCarrito(
                    plato.id,
                    plato.nombre,
                    plato.precio,
                    _cantidadPlatos.value ?: 1,
                    plato.urlImagen
                )
            withContext(Dispatchers.IO) {
                database.carritoDao.agregarPlatoAlCarrito(platoEnCarrito)
            }
            _navegacionAlListadoPlatos.value = true
        }
    }

    fun navegacionAlListadoPlatosTerminada() {
        _navegacionAlListadoPlatos.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewmodelJob.cancel()
    }

    class DetallePlatoViewModelFactory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetallePlatoViewModel::class.java))
                return DetallePlatoViewModel(application) as T
            throw IllegalArgumentException("ViewModel Desconocido")
        }
    }
}