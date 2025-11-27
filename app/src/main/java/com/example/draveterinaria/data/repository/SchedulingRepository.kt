package com.example.draveterinaria.data.repository

import com.example.draveterinaria.data.model.EspecieIdWrapper
import com.example.draveterinaria.data.remote.RetrofitClient
// Importar todos los Request y Response models
import com.example.draveterinaria.data.model.*

class SchedulingRepository {

    private val apiService = RetrofitClient.apiService

    // Funciones de obtención de listas (llamadas simples)
    suspend fun fetchEspecies(): List<EspecieResponse> = apiService.getEspecies()

    suspend fun fetchTiposServicio(): List<TipoServicioResponse> = apiService.getTiposServicio()

    suspend fun fetchSubtipos(idTipo: Long): List<SubtipoServicioResponse> {
        // La lógica de carga condicional ya está manejada por esta función.
        return apiService.getSubtiposByTipo(idTipo)
    }

    // Función de Transacción Final (Replica el 'submitServicio' de React)
    suspend fun completeScheduling(
        tutor: TutorInput,
        mascota: MascotaInput,
        servicio: ServicioInput // Usar tus modelos de UI (MascotaInput, etc.)
    ): String { // Retorna un mensaje de éxito

        // ------------------ 1. Registrar Tutor ------------------
        val tutorRequest = TutorRequest(
            runTutor = tutor.rut.substringBefore("-"),
            dvRun = tutor.rut.substringAfter("-"),
            nombreTutor = tutor.nombre,
            snombreTutor = tutor.snombre,
            apPaternoTutor = tutor.apaterno,
            aMaternoTutor = tutor.amaterno,
            telefono = tutor.telefono,
            direccion = tutor.direccion,
            email = tutor.email
        )
        val tutorResponse = apiService.createTutor(tutorRequest)
        val idTutor = tutorResponse.idTutor!! // Usar el ID retornado

        // ------------------ 2. Registrar Mascota ------------------
        val mascotaRequest = MascotaRequest(
            nombreMascota = mascota.nombre,
            fechaNacimiento = mascota.fechaNacimiento,
            sexo = mascota.sexo,
            raza = mascota.raza,
            antecedentes = mascota.antecedentes,
            especie = EspecieIdWrapper(idEspecie = mascota.especieId), // ID de la UI
            tutor = TutorIdWrapper(idTutor = idTutor) // ID obtenido
        )
        val mascotaResponse = apiService.createMascota(mascotaRequest)
        val idMascota = mascotaResponse.idMascota!! // Usar el ID retornado

        // ------------------ 3. Registrar Servicio ------------------
        val servicioRequest = ServicioRequest(
            mascota = MascotaIdWrapper(idMascota = idMascota), // ID obtenido
            subtipo = SubtipoIdWrapper(idSubtipo = servicio.subtipoId), // ID de la UI
            costo = servicio.precio.toDouble(), // Convertir a Double/Int
            fecha = "${servicio.fecha}T10:00:00" // Replicar el formato de fecha exacto
        )
        apiService.createServicio(servicioRequest)

        return "Registro completado con éxito,"
    }
}