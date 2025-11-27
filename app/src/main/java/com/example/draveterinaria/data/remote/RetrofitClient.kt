package com.example.draveterinaria.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {


    private const val BASE_URL = "http://10.0.2.2:8090/"

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