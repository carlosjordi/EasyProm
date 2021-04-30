package com.akipa.ui.gestion_pedidos

import androidx.lifecycle.*
import com.akipa.dto.Detalle
import com.akipa.dto.SolicitarCabecerasResponseItem
import com.akipa.dto.request.GestionarPedidoRequest
import com.akipa.entidades.CabeceraPedido
import com.akipa.network.AkipaAPI
import com.akipa.utils.Constantes.PEDIDO_GESTIONADO
import com.akipa.utils.obtenerCosteTotal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GestionPedidosViewModel : ViewModel() {

    private val _listaCabeceraPedidos = MutableLiveData<List<SolicitarCabecerasResponseItem>>()
    val listaCabeceraPedidos: LiveData<List<SolicitarCabecerasResponseItem>> = _listaCabeceraPedidos

    private val _cabeceraPedido = MutableLiveData<CabeceraPedido>()
    val cabeceraPedido: LiveData<CabeceraPedido> = _cabeceraPedido

    val gestionado = Transformations.map(_cabeceraPedido){
        it.gestionado == PEDIDO_GESTIONADO
    }

    private val _listaDetallePedido = MutableLiveData<List<Detalle>>()
    val listaDetallePedido: LiveData<List<Detalle>> = _listaDetallePedido

    val costeTotal = Transformations.map(_listaDetallePedido) {
        it.obtenerCosteTotal()
    }

    init {
        solicitarCabecerasPedidos()
    }

    fun solicitarCabecerasPedidos() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response =
                    AkipaAPI.retrofitService.solicitarTodasCabecerasPedidosAsync().await()

                withContext(Dispatchers.Main) {
                    _listaCabeceraPedidos.value = response.cabeceras
                }
            }
        }
    }

    fun solicitarDetallePedido(idPedido: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = AkipaAPI.retrofitService.solicitarDetallePedidoAsync(idPedido).await()
            withContext(Dispatchers.Main) {
                _listaDetallePedido.value = response.detalle
            }
        }
    }

    fun gestionarPedido(request: GestionarPedidoRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            AkipaAPI.retrofitService.gestionarPedidoAsync(
                request
            ).await()
            solicitarCabecerasPedidos()
        }
    }

    fun pedidoSeleccionado(cabecera: CabeceraPedido) {
        _cabeceraPedido.value = cabecera
    }
}