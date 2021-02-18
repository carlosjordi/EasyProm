package com.akipa.ui.carrito.pedido

import android.app.Application
import androidx.lifecycle.*
import com.akipa.database.AkipaLocalDatabase
import com.akipa.database.plato_en_carrito.PlatoEnCarrito
import com.akipa.dto.request.DeliveryRequest
import com.akipa.dto.request.RecojoEnTiendaRequest
import com.akipa.network.AkipaAPI
import com.akipa.utils.Constantes
import kotlinx.coroutines.*

class PedidosViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = AkipaLocalDatabase.getInstance(application.applicationContext)
    val platos = database.carritoDao.obtenerTodosPlatosDelCarrito()

    private var platoEnCarrito: List<PlatoEnCarrito>? = null

    private val _seTerminoOperacionCarrito = MutableLiveData<Boolean>()
    val seTerminoOperacionCarrito: LiveData<Boolean> = _seTerminoOperacionCarrito

    fun realizarPedidoTipoRecojoEnTienda(pedido: RecojoEnTiendaRequest) {
        coroutineScope.launch(Dispatchers.IO) {
            val registroPedido = AkipaAPI.retrofitService.registrarPedidoRecojoEnTiendaAsync(
                pedido.idLocal,
                pedido.idSolicitante,
                pedido.fechaRecojo,
                pedido.horaRecojo
            ).await()

            if (registroPedido.mensaje == Constantes.PEDIDO_REGISTRADO_MENSAJE_EXITOSO) {
                platoEnCarrito.let { lista ->
                    lista?.map { plato ->
                        AkipaAPI.retrofitService.agregarPlatoADetalleAsync(
                            registroPedido.idPedido,
                            plato.id,
                            plato.cantidad
                        ).await()
                    }
                    withContext(Dispatchers.Main) {
                        _seTerminoOperacionCarrito.value = true
                    }
                    database.carritoDao.quitarTodosLosPlatos()
                }
            }
        }
    }

    fun realizarPedidoTipoDelivery(pedido: DeliveryRequest) {
        coroutineScope.launch(Dispatchers.IO) {
            val registroPedido = AkipaAPI.retrofitService.registrarPedidoDeliveryAsync(
                pedido.idSolicitante,
                pedido.direccion,
                pedido.referencia
            ).await()

            if (registroPedido.mensaje == Constantes.PEDIDO_REGISTRADO_MENSAJE_EXITOSO) {
                platoEnCarrito.let { lista ->
                    lista?.map { plato ->
                        AkipaAPI.retrofitService.agregarPlatoADetalleAsync(
                            registroPedido.idPedido,
                            plato.id,
                            plato.cantidad
                        ).await()
                    }
                    withContext(Dispatchers.Main) {
                        _seTerminoOperacionCarrito.value = true
                    }
                    database.carritoDao.quitarTodosLosPlatos()
                }
            }
        }
    }

    fun operacionCarritoTerminada() {
        _seTerminoOperacionCarrito.value = false
    }

    fun asignarPlatosDelCarrito() {
        platoEnCarrito = platos.value
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

class PedidosViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PedidosViewModel::class.java))
            return PedidosViewModel(application) as T
        throw IllegalArgumentException("ViewModel Desconocido")
    }
}