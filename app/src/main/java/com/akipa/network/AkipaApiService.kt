package com.akipa.network

import com.akipa.dto.*
import com.akipa.dto.request.RegistrarPlatoRequest
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

    @FormUrlEncoded
    @POST("actualizarPlato.php")
    fun actualizarPlatoAsync(
        @Field("id") id: Int,
        @Field("nombre") nombre: String,
        @Field("precio") precio: Double,
        @Field("foto") foto: String,
        @Field("descripcion") descripcion: String?
    ): Deferred<PlatoRegistradoResponse>

    @FormUrlEncoded
    @POST("eliminarPlato.php")
    fun eliminarPlatoAsync(@Field("id") id: Int): Deferred<PlatoRegistradoResponse>

    @FormUrlEncoded
    @POST("registrarPedidoRecojoEnTienda.php")
    fun registrarPedidoRecojoEnTiendaAsync(
        @Field("id_local") idLocal: Int,
        @Field("id_solicitante") idSolicitante: String,
        @Field("fecha_recojo") fechaRecojo: String,
        @Field("hora_recojo") horaRecojo: String
    ): Deferred<RecojoEnTiendaResponse>

    @FormUrlEncoded
    @POST("registrarPedidoDelivery.php")
    fun registrarPedidoDeliveryAsync(
        @Field("id_solicitante") idSolicitante: String,
        @Field("direccion_entrega") direccion: String,
        @Field("referencia_entrega") referencia: String
    ): Deferred<DeliveryResponse>

    @FormUrlEncoded
    @POST("agregarPlatoADetalle.php")
    fun agregarPlatoADetalleAsync(
        @Field("id_pedido") idPedido: Int,
        @Field("id_plato") idPlato: Int,
        @Field("cantidad") cantidad: Int
    ): Deferred<PlatoAgregadoADetalleResponse>

    @GET("solicitarCabecerasMisPedidos.php")
    fun solicitarCabecerasMisPedidosAsync(
        @Query("id_solicitante") idSolicitante: String
    ): Deferred<SolicitarCabecerasResponse>

    @GET("solicitarDetalleMiPedido.php")
    fun solicitarDetallePedidoAsync(
        @Query("id_pedido") idPedido: Int
    ): Deferred<DetallePedidoResponse>

    @GET("solicitarCabecerasPedidos.php")
    fun solicitarTodasCabecerasPedidosAsync(): Deferred<SolicitarCabecerasResponse>

    @FormUrlEncoded
    @POST("gestionarPedido.php")
    fun gestionarPedidoAsync(
        @Field("id_pedido") idPedido: Int,
        @Field("observacion") observacion: String,
        @Field("estado") estado: Int
    ): Deferred<GestionPedidoResponse>
}