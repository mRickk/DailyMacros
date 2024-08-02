package com.example.dailymacros.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dailymacros.ui.screens.diary.Diary
import com.example.dailymacros.ui.screens.diet.Diet
import com.example.dailymacros.ui.screens.login.Login
import com.example.dailymacros.ui.screens.login.LoginViewModel
import com.example.dailymacros.ui.screens.overview.Overview
import com.example.dailymacros.ui.screens.profile.Profile
import com.example.dailymacros.ui.screens.search.Search
import com.example.dailymacros.ui.screens.settings.Settings
import com.example.dailymacros.ui.screens.settings.SettingsViewModel
import com.example.dailymacros.ui.screens.signup.Signup
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Login.route,
        modifier = modifier
    ) {

        composable(NavigationRoute.Login.route) {
            val loginViewModel = koinViewModel<LoginViewModel>()
            Login(navController)
        }
        composable(NavigationRoute.Signin.route) {
            Signup(navController)
        }
        composable(NavigationRoute.Diary.route) {
            Diary(navController)
        }
        composable(NavigationRoute.Search.route) {
            Search(navController)
        }
        composable(NavigationRoute.Profile.route) {
            Profile(navController)
        }
        composable(NavigationRoute.Diet.route) {
            Diet(navController)
        }
        composable(NavigationRoute.Overview.route) {
            Overview(navController)
        }
        composable(NavigationRoute.Settings.route) {
            val themeState by settingsViewModel.state.collectAsStateWithLifecycle()
            Settings(navController, themeState, settingsViewModel::changeTheme)
        }
        // AddFood inside SelectFood screen?
        // SelectAll and Add singular items?
        /*
        composable(NavigationRoute.AddFood.route) {
            AddFood(navController)
        }
        composable(NavigationRoute.SelectFood.route) {
            SelectFood(navController)
        }
        composable(NavigationRoute.AddExercise.route) {
            AddExercise(navController)
        }
        composable(NavigationRoute.SelectExercise.route) {
            SelectExercise(navController)
        }
        composable(NavigationRoute.AddRecipe.route) {
            AddRecipe(navController)
        }
        composable(NavigationRoute.SelectRecipe.route) {
            SelectRecipe(navController)
        }
        */
    }
}

sealed class NavigationRoute(
    val route: String
) {
    data object Login : NavigationRoute("Login")
    data object Signin : NavigationRoute("Signin")
    data object Diary : NavigationRoute("Diary")
    data object Search : NavigationRoute("Search")
    data object Profile : NavigationRoute("Profile")
    data object Diet : NavigationRoute("Diet")
    data object Overview : NavigationRoute("Overview")
    data object Settings : NavigationRoute("Settings")
    data object AddFood : NavigationRoute("AddFood")
    data object SelectFood : NavigationRoute("SelectFood")
    data object AddExercise : NavigationRoute("AddExercise")
    data object SelectExercise : NavigationRoute("SelectExercise")
    data object AddRecipe : NavigationRoute("AddRecipe")
    data object SelectRecipe : NavigationRoute("SelectRecipe")
}