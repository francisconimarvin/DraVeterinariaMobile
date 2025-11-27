package com.example.draveterinaria.ui.screens.forms

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.draveterinaria.data.model.EspecieResponse
import com.example.draveterinaria.data.model.MascotaInput
import com.example.draveterinaria.data.model.SEXOS // Asume que esta constante existe
import com.example.draveterinaria.ui.components.DropdownSelector // Importar el componente
import com.example.draveterinaria.ui.components.ErrorText // Componente utilitario para errores

@Composable
fun MascotaForm(
    mascotaInput: MascotaInput,
    especies: List<EspecieResponse>,
    errors: Map<String, String>, // Recibe los errores del ViewModel
    onValueChange: (MascotaInput) -> Unit,
    onNext: () -> Unit // Llama a validateMascota() y navega
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("1. Datos de la Mascota", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(4.dp))

            // Nombre
            OutlinedTextField(
                value = mascotaInput.nombre,
                onValueChange = { onValueChange(mascotaInput.copy(nombre = it)) },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                isError = errors.containsKey("nombre")
            )
            ErrorText(errors, "nombre")

            // Especie
            DropdownSelector(
                label = "Especie",
                options = especies.map { Pair(it.nombreEspecie, it.idEspecie) },
                selectedId = mascotaInput.especieId,
                onSelect = { id ->
                    onValueChange(mascotaInput.copy(especieId = id))
                }
            )
            ErrorText(errors, "especieId")

            // Raza
            OutlinedTextField(
                value = mascotaInput.raza,
                onValueChange = { onValueChange(mascotaInput.copy(raza = it)) },
                label = { Text("Raza") },
                modifier = Modifier.fillMaxWidth(),
                isError = errors.containsKey("raza")
            )
            ErrorText(errors, "raza")

            // Fecha de Nacimiento
            OutlinedTextField(
                value = mascotaInput.fechaNacimiento,
                onValueChange = { onValueChange(mascotaInput.copy(fechaNacimiento = it)) },
                label = { Text("Fecha Nacimiento (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                isError = errors.containsKey("fechaNacimiento")
            )
            ErrorText(errors, "fechaNacimiento")

            // Sexo
            DropdownSelector(
                label = "Sexo",
                options = SEXOS,
                selectedValue = mascotaInput.sexo,
                onSelect = { value ->
                    onValueChange(mascotaInput.copy(sexo = value))
                }
            )
            ErrorText(errors, "sexo")

            // Antecedentes (No obligatorio, no requiere validación aquí)
            OutlinedTextField(
                value = mascotaInput.antecedentes,
                onValueChange = { onValueChange(mascotaInput.copy(antecedentes = it)) },
                label = { Text("Antecedentes Médicos (Opcional)") },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                singleLine = false
            )

            Spacer(Modifier.height(8.dp))
            Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
                Text("Siguiente (Tutor)")
            }
        }
    }
}