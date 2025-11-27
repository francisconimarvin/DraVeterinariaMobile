package com.example.draveterinaria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState // Importar SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.draveterinaria.navigation.NavigationEvent
import com.example.draveterinaria.navigation.Screen
import com.example.draveterinaria.ui.screens.HomeScreen
import com.example.draveterinaria.ui.screens.LoginScreen
import com.example.draveterinaria.ui.screens.ProfileScreen
import com.example.draveterinaria.ui.screens.SchedulingScreen // ⭐️ Importar la pantalla de agendamiento
import com.example.draveterinaria.viewModels.MainViewModel
import com.example.draveterinaria.viewModels.SchedulingViewModel
import com.example.draveterinaria.ui.theme.DraVeterinariaTheme
import kotlinx.coroutines.flow.collectLatest


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DraVeterinariaTheme {
                val viewModel: MainViewModel = viewModel()
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(key1 = Unit) {
                    viewModel.navigationEvents.collectLatest { event ->
                        when(event){
                            is NavigationEvent.NavigateTo -> {
                                navController.navigate(event.route.route){
                                    event.popUpToRoute?.let{
                                        popUpTo(it.route){
                                            inclusive = event.inclusive
                                        }
                                    }
                                    launchSingleTop = event.singleTop
                                    restoreState = true
                                }
                            }
                            is NavigationEvent.PopBackStack -> navController.popBackStack()
                            is NavigationEvent.NavigateUp -> navController.navigateUp()
                        }
                    }
                }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),

                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Login.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Login.route) {
                            LoginScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(Screen.Home.route) {
                            HomeScreen(navController = navController, viewModel = viewModel)
                        }
                        // La línea duplicada de Screen.Home.route ha sido eliminada.
                        composable(route = Screen.Profile.route){
                            ProfileScreen(navController = navController, viewModel = viewModel)
                        }


                        composable(route = Screen.Scheduling.route) {
                            // Instanciamos el ViewModel que maneja todo el flujo
                            val schedulingViewModel: SchedulingViewModel = viewModel()

                            SchedulingScreen(
                                // Pasamos el ViewModel instanciado
                                viewModel = schedulingViewModel,
                                // Pasamos el estado del Snackbar
                                snackbarHostState = snackbarHostState
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GeneralPreview() {
    DraVeterinariaTheme {  }
}

// ACA ESTA LO ULTIMO QUE SE HIZO
/*class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            DraVeterinariaTheme {
                HomeScreenCompacta()
            }
        }
    }
}
*/