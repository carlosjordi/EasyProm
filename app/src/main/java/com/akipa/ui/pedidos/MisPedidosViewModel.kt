package com.akipa.ui.pedidos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akipa.dto.SolicitarCabecerasResponseItem
import com.akipa.network.AkipaAPI
import com.akipa.utils.UniqueIdentifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MisPedidosViewModel : ViewModel() {

    private val _listaPedidos = MutableLiveData<List<SolicitarCabecerasResponseItem>>()
    val listaPedidos: LiveData<List<SolicitarCabecerasResponseItem>> = _listaPedidos

    init {
        solicitarCabecerasPedidos()
    }

    fun solicitarCabecerasPedidos() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val id = UniqueIdentifier.getUniqueIdentifier()
                val response =
                    AkipaAPI.retrofitService.solicitarCabecerasMisPedidosAsync(id).await()

                withContext(Dispatchers.Main) {
                    _listaPedidos.value = response.cabeceras
                }
            }
        }
    }
}