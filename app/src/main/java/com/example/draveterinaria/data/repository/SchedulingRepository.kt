package com.example.draveterinaria.data.repository

import com.example.draveterinaria.data.model.EspecieIdWrapper
import com.example.draveterinaria.data.remote.RetrofitClient
import com.example.draveterinaria.data.model.*


class SchedulingRepository {

    private val apiService = RetrofitClient.schedulingApiService

    // Funciones de obtención de listas (llamadas simples)
    suspend fun fetchEspecies(): List<EspecieResponse> = apiService.getEspecies()

    suspend fun fetchTiposServicio(): List<TipoServicioResponse> = apiService.getTiposServicio()

    suspend fun fetchSubtipos(idTipo: Long): List<SubtipoServicioResponse> {
        return apiService.getSubtiposByTipo(idTipo)
    }

    suspend fun completeScheduling(
        tutor: TutorInput,
        mascota: MascotaInput,
        servicio: ServicioInput
    ): String { // Retorna un mensaje de éxito

        val rutLimpio = tutor.rut
            .replace(".", "")
            .replace(" ", "")
            .uppercase()

        // 2. Extracción segura del cuerpo numérico (RUN) y DV.
        val rutParts = rutLimpio.split('-')

        if (rutParts.size != 2) {
            // Debe tener el guion para separar el cuerpo numérico y el dígito verificador.
            throw IllegalArgumentException("Error: El RUT no tiene el formato esperado (cuerpo-DV).")
        }

        val runTutorValue = rutParts[0] // Cuerpo numérico (sin puntos)
        val dvRunValue = rutParts[1]    // Dígito Verificador

        // 3. Verificación de Límite de la Base de Datos (máximo 8 caracteres para el RUN)
        if (runTutorValue.length > 8) {
            // Se lanza una excepción que debe ser capturada y mostrada por el ViewModel.
            throw IllegalArgumentException("El número de RUN es demasiado largo (${runTutorValue.length} dígitos). El máximo permitido en la base de datos es 8. Verifique su entrada.")
        }


        // ------------------ 1. Registrar Tutor ------------------
        val tutorRequest = TutorRequest(
            // Se envían los valores limpios y separados:
            runTutor = runTutorValue,
            dvRun = dvRunValue,
            nombreTutor = tutor.nombre,
            snombreTutor = tutor.snombre,
            apPaternoTutor = tutor.apaterno,
            aMaternoTutor = tutor.amaterno,
            telefono = tutor.telefono,
            direccion = tutor.direccion,
            email = tutor.email
        )
        val tutorResponse = apiService.createTutor(tutorRequest)
        // Se asume que idTutor es un campo not-null en la respuesta si es exitosa.
        val idTutor = tutorResponse.idTutor!!

        // ------------------ 2. Registrar Mascota ------------------
        val mascotaRequest = MascotaRequest(
            nombreMascota = mascota.nombre,
            fechaNacimiento = mascota.fechaNacimiento,
            sexo = mascota.sexo,
            raza = mascota.raza,
            antecedentes = mascota.antecedentes,
            especie = EspecieIdWrapper(idEspecie = mascota.especieId),
            tutor = TutorIdWrapper(idTutor = idTutor) // ID obtenido
        )
        val mascotaResponse = apiService.createMascota(mascotaRequest)
        val idMascota = mascotaResponse.idMascota!!

        // ------------------ 3. Registrar Servicio ------------------
        val servicioRequest = ServicioRequest(
            mascota = MascotaIdWrapper(idMascota = idMascota), // ID obtenido
            subtipo = SubtipoIdWrapper(idSubtipo = servicio.subtipoId), // ID de la UI
            costo = servicio.precio.toDouble(), // Convertir a Double/Int
            // Asegurar que el formato de fecha cumpla con el estándar ISO 8601 (YYYY-MM-DDTHH:MM:SS)
            fecha = "${servicio.fecha}T10:00:00"
        )
        apiService.createServicio(servicioRequest)

        return "Registro completado con éxito, ID de Mascota: $idMascota"
    }
}