package com.example.draveterinaria.ui.screens.forms

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.draveterinaria.data.model.*
import com.example.draveterinaria.ui.components.DropdownSelector // Necesitarás este componente

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

            // Fecha (Usarías un DatePicker, por simplicidad usamos texto)
            OutlinedTextField(
                value = servicioInput.fecha,
                onValueChange = onFechaChange,
                label = { Text("Fecha (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

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