package com.example.draveterinaria.ui.screens.forms

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.draveterinaria.data.model.Tutor

@Composable
fun TutorForm(
    tutor: Tutor,
    registrado: Boolean,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Datos del Tutor", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = tutor.nombre,
                onValueChange = onNombreChange,
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !registrado
            )

            OutlinedTextField(
                value = tutor.correo,
                onValueChange = onCorreoChange,
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !registrado
            )

            OutlinedTextField(
                value = tutor.telefono,
                onValueChange = onTelefonoChange,
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !registrado
            )

            if (!registrado) {
                Button(onClick = onSubmit, modifier = Modifier.fillMaxWidth()) {
                    Text("Registrar Tutor")
                }
            } else {
                Text("✅ Tutor registrado", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
