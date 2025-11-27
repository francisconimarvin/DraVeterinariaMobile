package com.example.draveterinaria.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Componente simple para mostrar el mensaje de error debajo de un campo de formulario.
 * Solo se renderiza si el campo de la clave `fieldName` está presente en el mapa `errors`.
 * * @param errors Mapa de errores (clave: nombre del campo, valor: mensaje de error).
 * @param fieldName La clave del campo a verificar (ej: "nombre", "rut").
 */
@Composable
fun ErrorText(errors: Map<String, String>, fieldName: String) {
    if (errors.containsKey(fieldName)) {
        // Usamos !! porque ya verificamos con containsKey que el valor no es nulo.
        val errorMessage = errors[fieldName]!!

        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            // Aplicamos padding para alinear con el inicio del OutlinedTextField
            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
        )
    }
}