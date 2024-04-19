package com.example.quizgame.screen

//
sealed class Screen (val route: String){

    object Home: Screen(route= "home_screen")
    object Login: Screen(route= "login_screen")
    object Timer: Screen(route= "timer_screen")
    object Settings: Screen(route= "setting_screen")

}