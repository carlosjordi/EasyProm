package com.akipa.ui.carrito

import android.app.Application
import androidx.lifecycle.*
import com.akipa.database.AkipaLocalDatabase
import com.akipa.database.plato_en_carrito.PlatoEnCarrito
import com.akipa.database.plato_en_carrito.obtenerCosteTotal
import com.akipa.utils.Constantes
import kotlinx.coroutines.*

class CarritoViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = AkipaLocalDatabase.getInstance(application.applicationContext)

    /**
     * Lista de todos los platos agregados al carrito
     */
    val platosEnCarrito = database.carritoDao.obtenerTodosPlatosDelCarrito()

    private val _total = Transformations.map(platosEnCarrito) {
        it.obtenerCosteTotal()
    }
    val total = Transformations.map(_total) {
        "S/" + "%.2f".format(it)
    }
    val hayPlatosEnCarrito = Transformations.map(platosEnCarrito) {
        !it.isNullOrEmpty()
    }

    fun incrementarCantidadPlato(platoEnCarrito: PlatoEnCarrito) {

        if (platoEnCarrito.cantidad >= Constantes.CANTIDAD_PLATOS_MAXIMA)
            return

        platoEnCarrito.cantidad += 1
        coroutineScope.launch(Dispatchers.IO) {
            database.carritoDao.cambiarCantidadDePlatos(platoEnCarrito)
        }
    }

    fun reducirCantidadPlato(platoEnCarrito: PlatoEnCarrito) = coroutineScope.launch {
        if (platoEnCarrito.cantidad <= Constantes.CANTIDAD_PLATOS_MINIMA)
            return@launch

        platoEnCarrito.cantidad -= 1
        withContext(Dispatchers.IO) {
            database.carritoDao.cambiarCantidadDePlatos(platoEnCarrito)
        }
    }

    fun quitarPlato(platoEnCarrito: PlatoEnCarrito) = coroutineScope.launch(Dispatchers.IO) {
        database.carritoDao.quitarPlatoDelCarrito(platoEnCarrito)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Cuando un ViewModel tiene parametros se necesita crear un
     * Factory para que se le pueda interpretar. Este se encarga de
     * darnos un CarritoViewModel
     */
    class CarritoViewModelFactory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CarritoViewModel::class.java))
                return CarritoViewModel(application) as T
            throw IllegalArgumentException("ViewModel Desconocido")
        }
    }
}