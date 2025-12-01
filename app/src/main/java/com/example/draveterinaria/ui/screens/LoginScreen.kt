package com.example.draveterinaria.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.draveterinaria.R
import com.example.draveterinaria.navigation.Screen
import com.example.draveterinaria.viewModels.LoginState
import com.example.draveterinaria.viewModels.MainViewModel
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    // 1. Estados de la UI para capturar el input del usuario
    var email by remember { mutableStateOf("usuario@example.com") } // Valor inicial para prueba
    var password by remember { mutableStateOf("123456") } // Valor inicial para prueba

    // 2. Observación del estado de Login del ViewModel
    val loginState by viewModel.loginStatus.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // 3. Efecto para manejar eventos (navegación y errores)
    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is LoginState.Success -> {
                // ⭐️ Importante: Aquí es donde debes guardar state.token y state.role
                //    en DataStore o SharedPreferences para persistencia.

                // Navegar a Home después de guardar el token
                navController.navigate(Screen.Home.route) {
                    // Limpia la pila de navegación para que el usuario no pueda
                    // volver a la pantalla de login con el botón de atrás
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
                // Resetear el estado para que futuras llamadas no activen este LaunchedEffect
                viewModel.resetLoginStatus()
            }
            is LoginState.Error -> {
                // Mostrar el error en un Snackbar
                snackbarHostState.showSnackbar(state.message, actionLabel = "Cerrar")
                // Resetear el estado para que el error no se muestre si volvemos a esta pantalla
                viewModel.resetLoginStatus()
            }
            // Ignorar Idle y Loading
            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }, // Contenedor para el mensaje de error
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("DraVeterinaria Login") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo o imagen de la aplicación
            Image(
                painter = painterResource(id = R.drawable.ic_launcher), // Asegúrate de que este ID sea correcto
                contentDescription = "Logo DraVeterinaria",
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(150.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Campo de Email/Usuario
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email de Usuario") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Campo de Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de Iniciar Sesión
            Button(
                onClick = {
                    // Llamar a la función del ViewModel
                    if (email.isNotBlank() && password.isNotBlank()) {
                        viewModel.performLogin(email, password)
                    } else {
                        // Opcional: mostrar un Snackbar si los campos están vacíos
                        // launch { snackbarHostState.showSnackbar("Ingrese email y contraseña.") }
                    }
                },
                // Deshabilitar mientras la petición está cargando
                enabled = loginState !is LoginState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = when (loginState) {
                        LoginState.Loading -> "Verificando Credenciales..."
                        else -> "Iniciar Sesión"
                    }
                )
            }
        }
    }
}