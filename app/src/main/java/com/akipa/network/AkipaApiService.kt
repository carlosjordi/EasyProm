package com.akipa.network

import com.akipa.dto.*
import com.akipa.dto.request.*
import kotlinx.coroutines.Deferred
import retrofit2.http.*

/**
 * Interface donde estarán los servicios a consultar
 */
interface AkipaApiService {

    /**
     * Se llama al servicio cuando se inicia la aplicación y cada vez que
     * se realiza un swipe down del listado de platos
     */
    @GET("listarPlatos.php")
    fun obtenerListadoPlatosAsync(): Deferred<ListadoPlatos>

    @GET("inicioSesionPersonal.php")
    fun iniciarSesionAsync(
        @Query("usuario") usuario: String,
        @Query("contrasena") contrasena: String
    ): Deferred<PersonalAutorizado>

    /**
     * Se llama al servicio cuando se oprime el boton de 'agregar plato'
     * desde las opciones de gestión del administrador
     */
    @POST("registrarPlato.php")
    fun registrarPlatoAsync(
        @Body request: RegistrarPlatoRequest
    ): Deferred<PlatoRegistradoResponse>

    /**
     * Se llama al servicio cuando se oprime el boton de 'actualizar plato'
     * desde las opciones de gestion del administrador
     */
    @PUT("actualizarPlato.php")
    fun actualizarPlatoAsync(
        @Body request: ActualizarPlatoRequest
    ): Deferred<PlatoRegistradoResponse>

    /**
     * Se llama al servicio cuando se oprime el icono de eliminar(tacho)
     * ubicado en parte derecha de cada plato en el apartado de gestión
     * de platos del administrador
     */
    @PUT("eliminarPlato.php")
    fun eliminarPlatoAsync(@Body request: EliminarPlatoRequest): Deferred<PlatoRegistradoResponse>

    /**
     * Se llama al servicio cuando se oprime el boton de 'realizar pedido' en el modo de
     * 'Recojo en Tienda' desde la pantalla del 'Carrito'
     */
    @POST("registrarPedidoRecojoEnTienda.php")
    fun registrarPedidoRecojoEnTiendaAsync(
        @Body request: RegistrarPedidoRecojoTiendaRequest
    ): Deferred<RecojoEnTiendaResponse>

    /**
     * Se llama al servicio cuando se oprime el boton de 'realizar pedido'
     * en el modo de 'Delivery' desde la pantalla del 'Carrito'
     */
    @POST("registrarPedidoDelivery.php")
    fun registrarPedidoDeliveryAsync(
        @Body request: RegistrarPedidoDeliveryRequest
    ): Deferred<DeliveryResponse>

    /**
     * Se llama al servicio cuando se oprime el boton de 'realizar pedido' en
     * cualquiera de los 2 modos(Recojo en Tienda|Delivery) en la pantalla del
     * 'Carrito'tras haber registrado el pedido
     */
    @POST("agregarPlatoADetalle.php")
    fun agregarPlatoADetalleAsync(
        @Body request: AgregarPlatoADetalleRequest
    ): Deferred<PlatoAgregadoADetalleResponse>

    /**
     * Se llama al servicio cuando se va a la lista de 'mis pedidos'
     * en las opciones del menu de cliente
     */
    @GET("solicitarCabecerasMisPedidos.php")
    fun solicitarCabecerasMisPedidosAsync(
        @Query("id_solicitante") idSolicitante: String
    ): Deferred<SolicitarCabecerasResponse>

    /**
     * Se llama al servicio cuando se hace un tap a alguno de los pedidos realizados
     * por parte del cliente o cuando el cajero va a atender alguno de los pedidos
     */
    @GET("solicitarDetalleMiPedido.php")
    fun solicitarDetallePedidoAsync(
        @Query("id_pedido") idPedido: Int
    ): Deferred<DetallePedidoResponse>

    /**
     * Se llama al servicio cuando se está trabajando como cajero y se ingresa a
     * 'Gestión de pedidos'
     */
    @GET("solicitarCabecerasPedidos.php")
    fun solicitarTodasCabecerasPedidosAsync(): Deferred<SolicitarCabecerasResponse>

    /**
     * Se llama al servicio cuando se acepta o rechaza un pedido desde las opciones de gestión
     * del cajero
     */
    @PUT("gestionarPedido.php")
    fun gestionarPedidoAsync(
        @Body request: GestionarPedidoRequest
    ): Deferred<GestionPedidoResponse>
}