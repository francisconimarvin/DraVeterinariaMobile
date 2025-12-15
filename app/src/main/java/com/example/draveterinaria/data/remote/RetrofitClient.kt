package com.example.draveterinaria.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue

//  ==================== NOTA ====================== \\
// SE DEBE USAR LA IP 10.0.2.2 PARA CORRER EN EL EMULADOR
// LA IP DEBE SER CORRESPONDIENTE A LA DEL SERVIDOR EN EJECUCIÓN
private const val IP_ADDRESS = "http://10.0.2.2"

object RetrofitClient {

    // --- Definición de URLs por Servicio ---
    private const val BASE_URL_LOGIN = "$IP_ADDRESS:8095/"       // Puerto para LoginAPI
    private const val BASE_URL_SCHEDULING = "$IP_ADDRESS:8090/"  // Puerto para SchedulingAPI

    // --- Configuración Común del Cliente HTTP ---

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        // Nivel BODY para ver el JSON enviado y recibido (útil para debug)
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClient centralizado
    private val client: OkHttpClient = OkHttpClient.Builder()
        // Aquí se agregarían interceptores de autenticación JWT si ya tuviéramos el token.
        .addInterceptor(loggingInterceptor)
        .build()

    // --- Instancias de Retrofit por API (Diferente Puerto) ---

    // 1. Instancia para LoginAPI (Puerto 8095)
    private val loginRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_LOGIN)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    // 2. Instancia para SchedulingAPI (Puerto 8090)
    private val schedulingRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_SCHEDULING)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    // --- Servicios Expuestos ---

    // Servicio para la autenticación (LoginApiService)
    val loginApiService: LoginApiService by lazy {
        loginRetrofit.create(LoginApiService::class.java)
    }

    // Servicio para la programación de citas (SchedulingApiService)
    val schedulingApiService: SchedulingApiService by lazy {
        schedulingRetrofit.create(SchedulingApiService::class.java)
    }
}