package com.example.draveterinaria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.draveterinaria.navigation.NavigationEvent
import com.example.draveterinaria.navigation.Screen
import com.example.draveterinaria.ui.theme.PasteleriappTheme
import com.example.draveterinaria.ui.HomeScreen
import com.example.draveterinaria.ui.screens.LoginScreen
import com.example.draveterinaria.ui.screens.ProfileScreen
import com.example.draveterinaria.ui.screens.SettingScreen
import com.example.draveterinaria.viewModels.MainViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PasteleriappTheme {
                val viewModel: MainViewModel = viewModel()
                val navController = rememberNavController()

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
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Login.route,
                        modifier = Modifier.padding(innerPadding)
                    ){
                        composable(route = Screen.Login.route){
                            LoginScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(route = Screen.Home.route){
                            HomeScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(route = Screen.Profile.route){
                            ProfileScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(route = Screen.Setting.route){
                            SettingScreen(navController = navController, viewModel = viewModel)
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
    PasteleriappTheme {

    }
}

// ACA ESTA LO ULTIMO QUE SE HIZO
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            DraVeterinariaTheme {
                HomeScreenCompacta()
            }
        }
    }
}
/*
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DraVeterinariaTheme {
        Greeting("Android")
    }
}*/