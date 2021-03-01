package com.akipa.ui.mantenimiento_platos.gestion_platos.actualizar_plato

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.akipa.dto.request.ActualizarPlatoRequest
import com.akipa.network.AkipaAPI
import com.akipa.utils.Constantes
import kotlinx.coroutines.*

class ActualizarPlatoViewModel : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _platoActualizadoResultado = MutableLiveData<String?>(null)
    val platoActualizadoResultado: LiveData<String?>
        get() = _platoActualizadoResultado

    fun actualizarPlato(plato: ActualizarPlatoRequest) =
        coroutineScope.launch(Dispatchers.IO) {
            val resultado = AkipaAPI.retrofitService.actualizarPlatoAsync(
                plato
            ).await()
            if (resultado.mensaje == Constantes.PLATO_ACTUALIZADO_MENSAJE_EXITOSO) {
                withContext(Dispatchers.Main) {
                    _platoActualizadoResultado.value = resultado.mensaje
                }
            }
        }


    fun actualizarPlatoTerminado() {
        _platoActualizadoResultado.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}