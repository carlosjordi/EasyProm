package com.akipa.ui.gestion_pedidos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akipa.dto.SolicitarCabecerasResponseItem
import com.akipa.entidades.CabeceraPedido
import com.akipa.network.AkipaAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GestionPedidosViewModel : ViewModel() {

    private val _listaCabeceraPedidos = MutableLiveData<List<SolicitarCabecerasResponseItem>>()
    val listaCabeceraPedidos: LiveData<List<SolicitarCabecerasResponseItem>> = _listaCabeceraPedidos

    private val _cabeceraPedido = MutableLiveData<CabeceraPedido>()
    val cabeceraPedido: LiveData<CabeceraPedido> = _cabeceraPedido

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

    fun pedidoSeleccionado(cabecera: CabeceraPedido) {
        _cabeceraPedido.value = cabecera
    }
}