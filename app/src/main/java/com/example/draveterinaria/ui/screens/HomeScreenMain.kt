package com.example.draveterinaria.ui.screens

import androidx.compose.runtime.Composable
import com.example.draveterinaria.utils.obtenerWindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HomeScreenMain(){
    val windowSizeClass = obtenerWindowSizeClass()
    when (windowSizeClass.widthSizeClass){
        WindowWidthSizeClass.Compact -> HomeScreenCompacta()
        WindowWidthSizeClass.Medium -> HomeScreemMediana()
        WindowWidthSizeClass.Expanded -> HomeScreemExtendida()
    }
}

@Preview(name = "Compacta", widthDp = 360, heightDp = 800)
@Composable
fun PreviewCompacta(){
    HomeScreenCompacta()
}

@Preview(name = "Mediana", widthDp = 600, heightDp = 800)
@Composable
fun PreviewMediana(){
    HomeScreemMediana()
}

@Preview(name = "Extendida", widthDp = 840, heightDp = 800)
@Composable
fun PreviewExtendida(){
    HomeScreemExtendida()
}