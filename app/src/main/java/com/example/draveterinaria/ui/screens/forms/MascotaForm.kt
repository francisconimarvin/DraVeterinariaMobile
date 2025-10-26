package com.example.draveterinaria.ui.screens.forms

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.draveterinaria.data.model.Mascota

@Composable
fun MascotaForm(
    mascota: Mascota,
    registrado: Boolean,
    onNombreChange: (String) -> Unit,
    onEspecieChange: (String) -> Unit,
    onRazaChange: (String) -> Unit,
    onEdadChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Datos de la Mascota", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = mascota.nombre,
                onValueChange = onNombreChange,
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !registrado
            )

            OutlinedTextField(
                value = mascota.especie,
                onValueChange = onEspecieChange,
                label = { Text("Especie") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !registrado
            )

            OutlinedTextField(
                value = mascota.raza,
                onValueChange = onRazaChange,
                label = { Text("Raza") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !registrado
            )

            OutlinedTextField(
                value = mascota.edad,
                onValueChange = onEdadChange,
                label = { Text("Edad") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !registrado
            )

            if (!registrado) {
                Button(onClick = onSubmit, modifier = Modifier.fillMaxWidth()) {
                    Text("Registrar Mascota")
                }
            } else {
                Text("✅ Mascota registrada", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
