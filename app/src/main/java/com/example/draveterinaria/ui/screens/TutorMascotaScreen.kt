package com.example.draveterinaria.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf(Screen.Home, Screen.Profile)
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        label = { Text(screen.route) },
                        icon = {
                            Icon(
                                imageVector = when (screen) {
                                    Screen.Home -> Icons.Default.Home
                                    Screen.Profile -> Icons.Default.Person
                                    else -> Icons.Default.Menu
                                },
                                contentDescription = screen.route
                            )
                        }
                    )
                }
            }
        }
    )


    { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
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
                onSubmit = { mascotaViewModel.registrarMascota() }
            )
        }
    }
}

