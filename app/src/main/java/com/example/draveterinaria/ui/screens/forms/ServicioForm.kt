package com.example.draveterinaria.ui.screens.forms

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.draveterinaria.data.model.*
import com.example.draveterinaria.ui.components.DropdownSelector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Date
import java.util.Locale
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicioForm(
    servicioInput: ServicioInput,
    tiposServicio: List<TipoServicioResponse>,
    subtiposServicio: List<SubtipoServicioResponse>,
    isLoading: Boolean,
    onTipoChange: (Long) -> Unit,
    onSubtipoChange: (Long) -> Unit,
    onFechaChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("3. Detalles del Servicio", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(4.dp))

            // Tipo de Servicio (Dropdown)
            DropdownSelector(
                label = "Tipo de Servicio",
                options = tiposServicio.map { Pair(it.tipoServicio, it.idTipo) },
                selectedId = servicioInput.tipoId,
                onSelect = onTipoChange
            )

            // Subtipo de Servicio (Dropdown)
            DropdownSelector(
                label = "Subtipo de Servicio",
                options = subtiposServicio.map { Pair(it.nombreSubtipo, it.idSubtipo) },
                selectedId = servicioInput.subtipoId,
                onSelect = onSubtipoChange
            )


            // ---------- Fecha del Servicio (solo futuras) ----------
            var showDatePicker by remember { mutableStateOf(false) }
            var fechaError by remember { mutableStateOf<String?>(null) }

            val today = System.currentTimeMillis()

            val datePickerState = rememberDatePickerState(
                selectableDates = object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        return utcTimeMillis >= today
                    }
                }
            )

            OutlinedTextField(
                value = servicioInput.fecha,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha del Servicio") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                isError = fechaError != null,
                trailingIcon = {
                    IconButton(
                        onClick = { showDatePicker = true },
                        enabled = !isLoading
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Seleccionar fecha"
                        )
                    }
                }
            )

            if (fechaError != null) {
                Text(
                    text = fechaError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (showDatePicker && !isLoading) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    if (millis < today) {
                                        fechaError = "Solo se permiten fechas futuras"
                                    } else {
                                        val formatter = SimpleDateFormat(
                                            "yyyy-MM-dd",
                                            Locale.getDefault()
                                        )

                                        val fecha = formatter.format(Date(millis))

                                        fechaError = null
                                        onFechaChange(fecha)
                                    }
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

            // Costo/Precio
            Text("Costo del Servicio: $${servicioInput.precio}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = onBack, enabled = !isLoading) { Text("Atrás") }
                Button(
                    onClick = onSubmit,
                    enabled = !isLoading && servicioInput.subtipoId > 0 && servicioInput.fecha.isNotBlank()
                ) {
                    Text(if (isLoading) "Registrando..." else "Finalizar Registro")
                }
            }
        }
    }
}