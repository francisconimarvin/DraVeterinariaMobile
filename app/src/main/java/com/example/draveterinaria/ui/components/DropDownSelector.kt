package com.example.draveterinaria.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Componente DropdownSelector reutilizable para seleccionar un elemento de una lista de opciones
 * que tienen un Long como identificador (útil para IDs de la API).
 *
 * @param label El texto de la etiqueta a mostrar en el OutlinedTextField.
 * @param options Una lista de Pair<String, Long> donde String es el nombre visible y Long es el ID.
 * @param selectedId El ID actualmente seleccionado (usado para encontrar el nombre a mostrar).
 * @param onSelect La función que se llama cuando se selecciona una nueva opción, devolviendo el ID.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: String,
    options: List<Pair<String, Long>>,
    selectedId: Long,
    onSelect: (Long) -> Unit
) {
    // 1. Estado para controlar si el menú está abierto o cerrado
    var expanded by remember { mutableStateOf(false) }

    // 2. Determinar el nombre visible de la opción seleccionada
    val selectedOptionName = remember(selectedId, options) {
        // Busca el nombre de la opción basado en el ID seleccionado.
        options.find { it.second == selectedId }?.first ?: ""
    }

    // 3. Contenedor Exposado (OutlinedTextField + DropdownMenu)
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            // El campo de texto se convierte en el "anchor" del menú
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            readOnly = true,
            value = selectedOptionName,
            onValueChange = { /* No hay cambio directo por teclado */ },
            label = { Text(label) },
            trailingIcon = {
                // Icono para indicar que se puede desplegar
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expandir menú de ${label}",
                    modifier = Modifier.clickable { expanded = true }
                )
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )

        // 4. Menú Desplegable
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (options.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("Cargando datos...") },
                    onClick = { /* No hace nada */ },
                    enabled = false
                )
            } else {
                options.forEach { (name, id) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            // 5. Al hacer clic: Actualizar el estado, llamar a onSelect y cerrar el menú
                            onSelect(id)
                            expanded = false
                        },
                        // Marcar el elemento seleccionado
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}
/**
 * Sobrecarga del DropdownSelector para manejar selecciones que usan String como ID/Valor.
 *
 * @param options Una lista de Pair<String, String> donde String es el nombre visible y el valor.
 * @param selectedValue El valor actualmente seleccionado.
 * @param onSelect La función que se llama cuando se selecciona una nueva opción, devolviendo el String.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: String,
    options: List<Pair<String, String>>,
    selectedValue: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val selectedOptionName = remember(selectedValue, options) {
        options.find { it.second == selectedValue }?.first ?: ""
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            readOnly = true,
            value = selectedOptionName,
            onValueChange = { /* No hay cambio directo por teclado */ },
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expandir menú de ${label}",
                    modifier = Modifier.clickable { expanded = true }
                )
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { (name, value) ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = {
                        onSelect(value)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}