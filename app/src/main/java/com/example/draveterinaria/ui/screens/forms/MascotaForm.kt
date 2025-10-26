package com.example.draveterinaria.ui.screens.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Registro de Mascota", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = mascota.nombre,
                onValueChange = onNombreChange,
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = mascota.especie,
                onValueChange = onEspecieChange,
                label = { Text("Especie") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = mascota.raza,
                onValueChange = onRazaChange,
                label = { Text("Raza") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = mascota.edad,
                onValueChange = onEdadChange,
                label = { Text("Edad") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Registrar Mascota")
            }
        }
    }
}