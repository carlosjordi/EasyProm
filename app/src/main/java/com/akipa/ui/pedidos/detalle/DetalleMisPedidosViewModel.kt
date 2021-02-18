package com.akipa.ui.pedidos.detalle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akipa.dto.Detalle
import com.akipa.dto.DetallePedidoResponse
import com.akipa.network.AkipaAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetalleMisPedidosViewModel : ViewModel() {

    private val _detallePlatos = MutableLiveData<List<Detalle>>()
    val detallePlatos: LiveData<List<Detalle>> = _detallePlatos

    fun solicitarDetalle(idPedido: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = AkipaAPI.retrofitService.solicitarDetallePedidoAsync(idPedido).await()

            withContext(Dispatchers.Main) {
                _detallePlatos.value = response.detalle
            }
        }
    }
}