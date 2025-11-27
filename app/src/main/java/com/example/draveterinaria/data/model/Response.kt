package com.example.draveterinaria.data.model

// Modelo Especie (GET /api/especies)
data class EspecieResponse(
    val idEspecie: Long,
    val nombreEspecie: String
)

// Modelo TipoServicio (GET /api/tipos-servicio)
data class TipoServicioResponse(
    val idTipo: Long,
    val tipoServicio: String
)

// Modelo SubtipoServicio (GET /api/subtipos-servicio/tipo/{idTipo})
data class SubtipoServicioResponse(
    val idSubtipo: Long,
    val nombreSubtipo: String,
    val precio: Int // o Double, Long, según tu backend
)