package com.example.draveterinaria.navigation

sealed class Screen(val route:String) {

    data object Login : Screen("login_page")

    data object Home : Screen("home_page")

    data object Profile : Screen("profile_page")

    data object Setting : Screen("setting_page")

    data class Detail(val itemId: String) : Screen("detail_page/{itemId}"){

        fun buildRoute():String{
            return route.replace("{itemId}",itemId)
        }
    }
}