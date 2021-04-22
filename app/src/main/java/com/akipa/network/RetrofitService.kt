package com.akipa.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val HOST = "http://192.168.1.6"
const val BASE_URL = "$HOST/bd-Alison/ws/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

/**
 * Clase contenedora de la única instancia a usar para realizar
 * consultas a los servicios
 */
object AkipaAPI {

    /**
     * Instancia a utilizar para consultar servicios de Akipa
     */
    val retrofitService: AkipaApiService by lazy {
        retrofit.create(AkipaApiService::class.java)
    }
}