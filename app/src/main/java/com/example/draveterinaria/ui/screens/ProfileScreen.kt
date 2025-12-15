package com.example.draveterinaria.ui.screens


import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.draveterinaria.navigation.Screen
import com.example.draveterinaria.ui.notifications.NotificationHelper.showNotification
import com.example.draveterinaria.viewModels.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    // ---------------- CONTEXT ----------------
    val context = LocalContext.current

    // ---------------- DRAWER ----------------
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf(Screen.Home, Screen.Profile)
    var selectedItem by remember { mutableStateOf(1) }

    // ---------------- NOTIFICATION PERMISSION LAUNCHER ----------------
    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                showNotification(
                    context = context,
                    title = "Cita Veterinaria 🐾",
                    message = "Notificaciones activadas correctamente"
                )
            }
        }

    // ---------------- UI ----------------
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menú de perfil", modifier = Modifier.padding(16.dp))

                NavigationDrawerItem(
                    label = { Text("Ver mi perfil") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Profile)
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Configuración") },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() } }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Perfil") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    items.forEachIndexed { index, screen ->
                        NavigationBarItem(
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                                viewModel.navigateTo(screen)
                            },
                            label = { Text(screen.route) },
                            icon = {
                                Icon(
                                    imageVector = if (screen == Screen.Home)
                                        Icons.Default.Home
                                    else
                                        Icons.Default.Person,
                                    contentDescription = screen.route
                                )
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->

            // ---------------- CONTENT ----------------
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
            ) {

                Text("¡Bienvenido al Perfil!")

                // ---------------- BOTÓN NOTIFICACIÓN ----------------
                Button(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationPermissionLauncher.launch(
                                Manifest.permission.POST_NOTIFICATIONS
                            )
                        } else {
                            showNotification(
                                context = context,
                                title = "Cita Veterinaria 🐾",
                                message = "Esta es una notificación de prueba"
                            )
                        }
                    }
                ) {
                    Text("Probar notificación")
                }

            }
        }
    }
}