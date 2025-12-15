package com.example.draveterinaria.ui.screens

import androidx.compose.ui.graphics.Color
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.draveterinaria.R
import com.example.draveterinaria.navigation.Screen
import com.example.draveterinaria.viewModels.LoginState
import com.example.draveterinaria.viewModels.MainViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val loginState by viewModel.loginStatus.collectAsState()

    // Estado para mostrar animación de éxito
    var showSuccessAnim by remember { mutableStateOf(false) }

    // Lanzar animación y navegación cuando login es exitoso
    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            navController.navigate(Screen.DeviceAuth.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
            viewModel.resetLoginStatus()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("DraVeterinaria Login") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher),
                    contentDescription = "Logo DraVeterinaria",
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(150.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(32.dp))

                // EMAIL
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = null
                    },
                    label = { Text("Email de Usuario") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                    isError = emailError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                emailError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // PASSWORD
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null
                    },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                    isError = passwordError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                passwordError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // BOTÓN LOGIN
                Button(
                    onClick = {
                        emailError = null
                        passwordError = null
                        viewModel.resetLoginStatus()

                        var valid = true
                        if (email.isBlank()) { emailError = "Ingrese su email"; valid = false }
                        if (password.isBlank()) { passwordError = "Ingrese su contraseña"; valid = false }

                        if (valid) viewModel.performLogin(email, password)
                    },
                    enabled = loginState !is LoginState.Loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = if (loginState is LoginState.Loading)
                            "Verificando credenciales..."
                        else
                            "Iniciar Sesión"
                    )
                }

                // ERROR LOGIN
                AnimatedVisibility(visible = loginState is LoginState.Error) {
                    Text(
                        text = (loginState as? LoginState.Error)?.message.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth()
                    )
                }
            }

            // ------------------ Animación de éxito ------------------
            if (showSuccessAnim) {
                SuccessCheckAnimation(visible = showSuccessAnim)
            }
        }
    }
}

@Composable
fun SuccessCheckAnimation(visible: Boolean) {
    var showCheck by remember { mutableStateOf(visible) }
    val progress by animateFloatAsState(
        targetValue = if (showCheck) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(visible) {
        if (visible) {
            showCheck = true
            delay(1500)
            showCheck = false
        }
    }

    AnimatedVisibility(showCheck) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF4CAF50),
                modifier = Modifier.size(120.dp)
            ) {}

            Canvas(modifier = Modifier.size(80.dp)) {
                val width = size.width
                val height = size.height
                val startX = width * 0.15f
                val startY = height * 0.5f
                val midX = width * 0.4f
                val midY = height * 0.75f
                val endX = width * 0.85f
                val endY = height * 0.25f

                if (progress < 0.5f) {
                    val p = progress / 0.5f
                    drawLine(
                        color = Color.White,
                        strokeWidth = 8f,
                        start = Offset(startX, startY),
                        end = Offset(startX + (midX - startX) * p, startY + (midY - startY) * p)
                    )
                } else {
                    drawLine(
                        color = Color.White,
                        strokeWidth = 8f,
                        start = Offset(startX, startY),
                        end = Offset(midX, midY)
                    )
                    val p = (progress - 0.5f) / 0.5f
                    drawLine(
                        color = Color.White,
                        strokeWidth = 8f,
                        start = Offset(midX, midY),
                        end = Offset(midX + (endX - midX) * p, midY + (endY - midY) * p)
                    )
                }
            }
        }
    }
}
