package com.example.draveterinaria.ui.screens

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.example.draveterinaria.security.BiometricAuthManager
import com.example.draveterinaria.security.DeviceSecurityUtils
import com.example.draveterinaria.viewModels.MainViewModel
import com.example.draveterinaria.navigation.Screen

@Composable
fun DeviceAuthScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val activity = context as FragmentActivity
    val biometric = remember { BiometricAuthManager(activity) }

    LaunchedEffect(Unit) {
        biometric.authenticate(
            onSuccess = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.DeviceAuth.route) { inclusive = true }
                }
            },
            onError = {
                viewModel.logout()
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.DeviceAuth.route) { inclusive = true }
                }
            }
        )
    }
}


