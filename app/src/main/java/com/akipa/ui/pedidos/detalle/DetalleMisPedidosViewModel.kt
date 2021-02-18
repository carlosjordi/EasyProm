package com.akipa.ui.pedidos.detalle

import androidx.lifecycle.*
import com.akipa.dto.Detalle
import com.akipa.dto.DetallePedidoResponse
import com.akipa.network.AkipaAPI
import com.akipa.utils.obtenerCosteTotal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetalleMisPedidosViewModel : ViewModel() {

    private val _detallePlatos = MutableLiveData<List<Detalle>>()
    val detallePlatos: LiveData<List<Detalle>> = _detallePlatos

    val costeTotal = Transformations.map(_detallePlatos) {
        it.obtenerCosteTotal()
    }

    fun solicitarDetalle(idPedido: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = AkipaAPI.retrofitService.solicitarDetallePedidoAsync(idPedido).await()

            withContext(Dispatchers.Main) {
                _detallePlatos.value = response.detalle
            }
        }
    }
}