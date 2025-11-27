package com.example.draveterinaria.data.remote

import com.example.draveterinaria.data.model.* // Tus Data Classes
import retrofit2.http.*
interface SchedulingApiService {

    // ----------------- Peticiones GET (Carga de Datos Iniciales) -----------------

    @GET("api/tipos-servicio")
    suspend fun getTiposServicio(): List<TipoServicioResponse>

    @GET("api/especies")
    suspend fun getEspecies(): List<EspecieResponse>

    // Petición condicional (similar al useEffect de React)
    @GET("api/subtipos-servicio/tipo/{idTipo}")
    suspend fun getSubtiposByTipo(@Path("idTipo") idTipo: Long): List<SubtipoServicioResponse>

    // ----------------- Peticiones POST (Transacción Secuencial) -----------------

    @POST("api/tutores")
    suspend fun createTutor(@Body tutor: TutorRequest): TutorResponse

    @POST("api/mascotas")
    suspend fun createMascota(@Body mascota: MascotaRequest): MascotaResponse

    // La función que realiza el POST final
    @POST("api/servicios/servicios")
    suspend fun createServicio(@Body servicio: ServicioRequest) // Asume que no devuelve cuerpo
}