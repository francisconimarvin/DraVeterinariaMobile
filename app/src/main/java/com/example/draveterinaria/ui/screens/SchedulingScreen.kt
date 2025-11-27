package com.example.draveterinaria.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.draveterinaria.ui.screens.forms.MascotaForm
import com.example.draveterinaria.ui.screens.forms.TutorForm
import com.example.draveterinaria.ui.screens.forms.ServicioForm
import com.example.draveterinaria.viewModels.SchedulingViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SchedulingScreen(
    viewModel: SchedulingViewModel = viewModel(),
    snackbarHostState: SnackbarHostState // Se pasa desde el Scaffold
) {
    // Estado local para manejar el paso (step) actual
    var currentStep by remember { mutableStateOf(1) }

    // 1. OBSERVAR ESTADOS DEL VIEWMODEL (Datos, Carga y Errores)
    val mascotaState by viewModel.mascotaInput.collectAsState()
    val tutorState by viewModel.tutorInput.collectAsState()
    val servicioState by viewModel.servicioInput.collectAsState()
    val isLoading by viewModel.loading.collectAsState()

    val especiesList by viewModel.especies.collectAsState()
    val tiposServicioList by viewModel.tiposServicio.collectAsState()
    val subtiposList by viewModel.subtiposServicio.collectAsState()

    val validationErrors by viewModel.validationErrors.collectAsState()

    // Manejar el resultado de la transacción (éxito/error)
    LaunchedEffect(Unit) {
        viewModel.registrationStatus.collectLatest { status ->
            // Muestra el mensaje de éxito o error en el Snackbar
            snackbarHostState.showSnackbar(status)
            if (status.contains("éxito")) {
                currentStep = 1 // Vuelve al inicio después de un éxito
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Aquí puedes agregar un componente visual para el progreso (ej. un Row con iconos de pasos)

            when (currentStep) {
                1 -> MascotaForm(
                    mascotaInput = mascotaState,
                    especies = especiesList,
                    errors = validationErrors,
                    onValueChange = viewModel::updateMascotaInput,
                    onNext = {
                        if (viewModel.validateMascota()) {
                            viewModel.clearErrors() // Limpia errores al avanzar
                            currentStep = 2
                        }
                    }
                )
                2 -> TutorForm(
                    tutorInput = tutorState,
                    errors = validationErrors,
                    onValueChange = viewModel::updateTutorInput,
                    onNext = {

                        if (viewModel.validateTutor()) {
                            viewModel.clearErrors() // Limpia errores al avanzar
                            currentStep = 3
                        }
                    },
                    onBack = {
                        viewModel.clearErrors() // Limpia errores al retroceder
                        currentStep = 1
                    }
                )
                3 -> ServicioForm(
                    servicioInput = servicioState,
                    tiposServicio = tiposServicioList,
                    subtiposServicio = subtiposList,
                    isLoading = isLoading,
                    // Estos no usan 'onValueChange' porque son IDs/Fechas manejadas con lógica condicional en el ViewModel
                    onTipoChange = viewModel::onServicioTipoChange,
                    onSubtipoChange = viewModel::onServicioSubtipoChange,
                    onFechaChange = viewModel::onServicioFechaChange,
                    onSubmit = viewModel::submitRegistration, // Ejecuta la transacción final
                    onBack = {
                        viewModel.clearErrors() // Limpia errores al retroceder
                        currentStep = 2
                    }
                )
            }
        }
    }
}

