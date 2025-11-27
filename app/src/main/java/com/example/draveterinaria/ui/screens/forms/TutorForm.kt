package com.example.draveterinaria.ui.screens.forms

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.draveterinaria.data.model.TutorInput
import com.example.draveterinaria.ui.components.ErrorText // Importar el componente

@Composable
fun TutorForm(
    tutorInput: TutorInput,
    errors: Map<String, String>, // Recibe los errores del ViewModel
    onValueChange: (TutorInput) -> Unit,
    onNext: () -> Unit, // Llama a validateTutor() y navega
    onBack: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("2. Datos del Tutor", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(4.dp))

            // RUT
            OutlinedTextField(
                value = tutorInput.rut,
                onValueChange = { onValueChange(tutorInput.copy(rut = it)) },
                label = { Text("RUT (Ej: 12345678-9)") },
                modifier = Modifier.fillMaxWidth(),
                isError = errors.containsKey("rut")
            )
            ErrorText(errors, "rut")

            // Nombre
            OutlinedTextField(
                value = tutorInput.nombre,
                onValueChange = { onValueChange(tutorInput.copy(nombre = it)) },
                label = { Text("Primer Nombre") },
                modifier = Modifier.fillMaxWidth(),
                isError = errors.containsKey("nombre")
            )
            ErrorText(errors, "nombre")

            // Snombre
            OutlinedTextField(
                value = tutorInput.snombre,
                onValueChange = { onValueChange(tutorInput.copy(snombre = it)) },
                label = { Text("Segundo Nombre (Opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
            // Snombre no es obligatorio, no necesita ErrorText

            // Apellido Paterno
            OutlinedTextField(
                value = tutorInput.apaterno,
                onValueChange = { onValueChange(tutorInput.copy(apaterno = it)) },
                label = { Text("Apellido Paterno") },
                modifier = Modifier.fillMaxWidth(),
                isError = errors.containsKey("apaterno")
            )
            ErrorText(errors, "apaterno")

            // Apellido Materno
            OutlinedTextField(
                value = tutorInput.amaterno,
                onValueChange = { onValueChange(tutorInput.copy(amaterno = it)) },
                label = { Text("Apellido Materno (Opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            // Teléfono
            OutlinedTextField(
                value = tutorInput.telefono,
                onValueChange = { onValueChange(tutorInput.copy(telefono = it)) },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                isError = errors.containsKey("telefono")
            )
            ErrorText(errors, "telefono")

            // Dirección
            OutlinedTextField(
                value = tutorInput.direccion,
                onValueChange = { onValueChange(tutorInput.copy(direccion = it)) },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth(),
                isError = errors.containsKey("direccion")
            )
            ErrorText(errors, "direccion")

            // Email
            OutlinedTextField(
                value = tutorInput.email,
                onValueChange = { onValueChange(tutorInput.copy(email = it)) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = errors.containsKey("email")
            )
            ErrorText(errors, "email")

            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = onBack) { Text("Atrás") }
                Button(onClick = onNext) { Text("Siguiente (Servicio)") }
            }
        }
    }
}