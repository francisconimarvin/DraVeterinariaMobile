package com.example.draveterinaria.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.* // Asegúrate de que esta sea la importación de Material3
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.draveterinaria.ui.screens.forms.MascotaForm
import com.example.draveterinaria.ui.screens.forms.TutorForm
import com.example.draveterinaria.viewModels.MascotaViewModel
import com.example.draveterinaria.viewModels.TutorViewModel
import com.example.draveterinaria.viewModels.MainViewModel
import com.example.draveterinaria.navigation.Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorMascotaScreen(
    navController: NavController,
    mainViewModel: MainViewModel = viewModel(),
    tutorViewModel: TutorViewModel = viewModel(),
    mascotaViewModel: MascotaViewModel = viewModel()
) {
    val tutorState by tutorViewModel.tutor.collectAsState()
    val registrado by tutorViewModel.registrado.collectAsState()
    val mascotaState by mascotaViewModel.mascota.collectAsState()
    val mascotaRegistrada by mascotaViewModel.registrado.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Registro de Tutor y Mascota") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TutorForm(
                tutor = tutorState,
                registrado = registrado,
                onNombreChange = tutorViewModel::onNombreChange,
                onCorreoChange = tutorViewModel::onCorreoChange,
                onTelefonoChange = tutorViewModel::onTelefonoChange,
                onSubmit = { tutorViewModel.registrarTutor() }
            )

            Divider()

            MascotaForm(
                mascota = mascotaState,
                registrado = mascotaRegistrada,
                onNombreChange = mascotaViewModel::onNombreChange,
                onEspecieChange = mascotaViewModel::onEspecieChange,
                onRazaChange = mascotaViewModel::onRazaChange,
                onEdadChange = { mascotaViewModel.onEdadChange(it) },
                onSubmit = {
                    mascotaViewModel.registrarMascota()
                    // Navegar a Home después de registrar
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true } // limpia back stack
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
