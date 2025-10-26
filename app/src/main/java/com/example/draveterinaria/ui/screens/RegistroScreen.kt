package com.example.draveterinaria.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.draveterinaria.viewModels.TutorViewModel
import com.example.draveterinaria.viewModels.MascotaViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import com.example.draveterinaria.ui.screens.forms.MascotaForm
import com.example.draveterinaria.ui.screens.forms.TutorForm
import androidx.navigation.NavController
import androidx.compose.runtime.LaunchedEffect
import com.example.draveterinaria.navigation.Screen
@Composable
fun RegistroScreen(
    navController: NavController,
    tutorViewModel: TutorViewModel = viewModel(),
    mascotaViewModel: MascotaViewModel = viewModel()
) {
    val tutor by tutorViewModel.tutor.collectAsState()
    val registrado by tutorViewModel.registrado.collectAsState()
    val mascota by mascotaViewModel.mascota.collectAsState()
    val mascotaRegistrada by mascotaViewModel.registrado.collectAsState()

    // Cuando mascotaRegistrada cambia a true, navegamos
    if (mascotaRegistrada) {
        LaunchedEffect(mascotaRegistrada) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        TutorForm(
            tutor = tutor,
            registrado = registrado,
            onNombreChange = tutorViewModel::onNombreChange,
            onCorreoChange = tutorViewModel::onCorreoChange,
            onTelefonoChange = tutorViewModel::onTelefonoChange,
            onSubmit = { tutorViewModel.registrarTutor() }
        )

        MascotaForm(
            mascota = mascota,
            onNombreChange = mascotaViewModel::onNombreChange,
            onEspecieChange = mascotaViewModel::onEspecieChange,
            onRazaChange = mascotaViewModel::onRazaChange,
            onEdadChange = mascotaViewModel::onEdadChange,
            onSubmit = { mascotaViewModel.registrarMascota() }
        )
    }
}
