package com.example.draveterinaria.ui.screens

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.example.draveterinaria.security.BiometricAuthManager
import com.example.draveterinaria.viewModels.MainViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val activity = context as FragmentActivity
    val biometric = remember { BiometricAuthManager(activity) }

    LaunchedEffect(Unit) {
        if (viewModel.hasSession() && biometric.canAuthenticate()) {

            biometric.authenticate(
                onSuccess = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onError = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )

        } else {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }
}
