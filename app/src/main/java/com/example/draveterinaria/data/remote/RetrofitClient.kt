package com.example.draveterinaria.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // ⚠️ ¡IMPORTANTE! Cambia esta IP:
    // 10.0.2.2 es la dirección especial para acceder al localhost de tu PC desde el EMULADOR de Android.
    // Si estás usando un dispositivo físico, debes usar la IP de tu PC en la red local (ej. http://192.168.1.XX:8090/)
    private const val BASE_URL = "http://10.0.2.2:8090/"

    // Interceptor para ver las peticiones y respuestas en Logcat (muy útil para debuggear)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Conversor de JSON a Kotlin
            .client(client)
            .build()
    }

    // Inicializa tu interfaz de servicio API (que define tus @GET y @POST)
    val apiService: SchedulingApiService by lazy {
        retrofit.create(SchedulingApiService::class.java)
    }
}