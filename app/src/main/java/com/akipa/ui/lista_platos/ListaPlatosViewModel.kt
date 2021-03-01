package com.akipa.ui.lista_platos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.akipa.dto.ListadoPlatos
import com.akipa.dto.request.EliminarPlatoRequest
import com.akipa.entidades.Plato
import com.akipa.network.AkipaAPI
import com.akipa.utils.Constantes
import kotlinx.coroutines.*

/**
 * Nos indicará si la info de los platos se encuentra
 * en alguno de los estados establecidos.
 * CARGANDO -> aún se está obteniendo
 * ERROR -> no se pudo conseguir la info
 * LISTO -> información conseguida y lista para uso
 */
enum class EstadoListadoPlatos { CARGANDO, ERROR, LISTO }

class ListaPlatosViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _listadoPlatos = MutableLiveData<List<Plato>>()
    val listadoPlatos: LiveData<List<Plato>>
        get() = _listadoPlatos

    private val _estadoListaPlatos = MutableLiveData<EstadoListadoPlatos>()
    val estadoListaPlatos: LiveData<EstadoListadoPlatos>
        get() = _estadoListaPlatos

    init {
        obtenerListadoPlatos()
    }

    fun obtenerListadoPlatos() =
        coroutineScope.launch {
            _estadoListaPlatos.value = EstadoListadoPlatos.CARGANDO
            lateinit var listado: ListadoPlatos
            withContext(Dispatchers.IO) {
                listado = AkipaAPI.retrofitService.obtenerListadoPlatosAsync().await()
            }
            try {
                _estadoListaPlatos.value = EstadoListadoPlatos.LISTO
                if (listado.platos.isNotEmpty()) {
                    _listadoPlatos.value = listado.platos
                }
            } catch (e: Exception) {
                _estadoListaPlatos.value = EstadoListadoPlatos.ERROR
                _listadoPlatos.value = ArrayList()
            }
        }

    fun eliminarPlato(plato: EliminarPlatoRequest) {
        coroutineScope.launch(Dispatchers.IO) {
            val respuesta = AkipaAPI.retrofitService.eliminarPlatoAsync(plato).await()
            if (respuesta.mensaje == Constantes.PLATO_ELIMINADO_MENSAJE_EXITOSO) {
                obtenerListadoPlatos()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}