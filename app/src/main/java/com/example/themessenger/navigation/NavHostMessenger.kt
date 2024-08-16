package com.example.themessenger.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.themessenger.MainViewModel
import com.example.themessenger.screens.chats.ChatsScreen
import com.example.themessenger.screens.login.ConfirmScreen
import com.example.themessenger.screens.login.LoginScreen
import com.example.themessenger.screens.profile.ProfileScreen
import com.example.themessenger.screens.register.RegisterScreen
import com.example.themessenger.utils.Constants

@Composable
fun NavHostMessenger(mViewModel: MainViewModel, navController: NavHostController) {

    NavHost(navController = navController, startDestination = NavRoute.Login.route) {
        composable(NavRoute.Login.route) {
            LoginScreen(
                navController = navController,
                viewModel = mViewModel
            )
        }
        composable(NavRoute.Confirm.route) {
            ConfirmScreen(
                navController = navController,
                viewModel = mViewModel
            )
        }
        composable(NavRoute.Register.route) {
            RegisterScreen(
                navController = navController,
                viewModel = mViewModel
            )
        }
        composable(NavRoute.Chats.route) {
            ChatsScreen(
                navController = navController,
                viewModel = mViewModel
            )
        }
        composable(NavRoute.Chat.route) {
            ChatsScreen(
                navController = navController,
                viewModel = mViewModel
            )
        }
        composable(NavRoute.Profile.route) {
            ProfileScreen(
                navController = navController,
                viewModel = mViewModel
            )
        }
    }
}

sealed class NavRoute(val route: String) {
    data object Login: NavRoute(Constants.Screens.LOGIN_SCREEN)
    data object Confirm: NavRoute(Constants.Screens.CONFIRM_SCREEN)
    data object Register: NavRoute(Constants.Screens.REGISTER_SCREEN)
    data object Chats: NavRoute(Constants.Screens.CHATS_SCREEN)
    data object Chat: NavRoute(Constants.Screens.CHAT_SCREEN)
    data object Profile: NavRoute(Constants.Screens.PROFILE_SCREEN)
}