package com.example.quizgame

import LoginScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quizgame.backend.RetrofitClient
import com.example.quizgame.screen.Screen

@Composable
fun SetupNavGraph (
    navController: NavHostController
){
    val apiService = RetrofitClient.apiService
    NavHost(navController = navController, startDestination = Screen.Login.route){

        composable(

            route = Screen.Login.route
        ){
            LoginScreen(apiService =apiService,navController )
        }
        composable(
            route = Screen.Home.route
        ){
            TreeView(
                treeImage = painterResource(id = R.drawable.tree),
                navController
            )
        }
        composable(
            route = Screen.Timer.route
        ){
            TreeViewFocus(
                treeImage = painterResource(id = R.drawable.tree),
                timerInitialValue=1000,
                navController = navController
            )
        }

    }

}