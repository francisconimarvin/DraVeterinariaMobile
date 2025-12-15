package com.example.draveterinaria.utils

import org.json.JSONObject

fun parseErrorMessage(errorBody: String?): String {
    if (errorBody.isNullOrBlank()) {
        return "Credenciales inválidas"
    }

    return try {
        val json = JSONObject(errorBody)
        json.optString("message", "Credenciales inválidas")
    } catch (e: Exception) {
        "Credenciales inválidas"
    }
}