package com.akipa.ui.mantenimiento_platos.agregar_plato

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.akipa.dto.request.RegistrarPlatoRequest
import com.akipa.network.AkipaAPI
import com.akipa.utils.Constantes
import kotlinx.coroutines.*

class AgregarPlatoViewModel : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _platoRegistradoResultado = MutableLiveData<String?>(null)
    val platoRegistradoResultado: LiveData<String?>
        get() = _platoRegistradoResultado

    fun registrarPlato(plato: RegistrarPlatoRequest) =
        coroutineScope.launch(Dispatchers.IO) {
            val resultado = AkipaAPI.retrofitService.registrarPlatoAsync(
                plato
            ).await()
            if (resultado.mensaje == Constantes.PLATO_REGISTRADO_MENSAJE_EXITOSO) {
                withContext(Dispatchers.Main) {
                    _platoRegistradoResultado.value = resultado.mensaje
                }
            }
        }

    fun registrarPlatoTerminado() {
        _platoRegistradoResultado.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}