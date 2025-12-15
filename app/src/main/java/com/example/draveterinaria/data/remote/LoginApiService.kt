package com.example.draveterinaria.data.remote

import com.example.draveterinaria.data.model.LoginRequest
import com.example.draveterinaria.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interfaz de servicio que define el contrato con la API de autenticación (puerto 8095).
 * Retrofit utiliza estas anotaciones para construir las peticiones HTTP.
 */
interface LoginApiService {

    /**
     * Endpoint para iniciar sesión.
     * * @param request: Contiene el email y la password (se serializa a JSON en el cuerpo).
     * @return Response<LoginResponse>: Contiene el JWT y el rol si es exitoso (código 200).
     */
    @POST("/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>


}