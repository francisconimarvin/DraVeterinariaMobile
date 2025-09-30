package com.example.draveterinaria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.draveterinaria.ui.theme.DraVeterinariaTheme
<<<<<<< HEAD
import com.example.draveterinaria.ui.screens.HomeScreen
=======
import com.example.draveterinaria.ui.theme.HomeScreen
import com.example.draveterinaria.ui.screens.HomeScreenCompacta
>>>>>>> 8bcdc5a4e8469ff081cb920b475f2174f2139dc3
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            DraVeterinariaTheme {
<<<<<<< HEAD

                HomeScreen2()
=======
                HomeScreen()
                HomeScreenCompacta()
>>>>>>> 8bcdc5a4e8469ff081cb920b475f2174f2139dc3
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