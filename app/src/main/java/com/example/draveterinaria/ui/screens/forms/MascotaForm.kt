package com.example.draveterinaria.ui.screens.forms

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import java.time.Instant
import java.time.ZoneId



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
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

            // Fecha de Nacimiento (con calendario)
            var showDatePicker by remember { mutableStateOf(false) }
            val datePickerState = rememberDatePickerState()

            OutlinedTextField(
                value = mascotaInput.fechaNacimiento,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha de Nacimiento") },
                modifier = Modifier.fillMaxWidth(),
                isError = errors.containsKey("fechaNacimiento"),
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Seleccionar fecha"
                        )
                    }
                }
            )

            ErrorText(errors, "fechaNacimiento")

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    val fecha = Instant.ofEpochMilli(millis)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                                        .toString() // YYYY-MM-DD

                                    onValueChange(
                                        mascotaInput.copy(fechaNacimiento = fecha)
                                    )
                                }
                                showDatePicker = false
                            }
                        ) {
                            Text("Aceptar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancelar")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

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