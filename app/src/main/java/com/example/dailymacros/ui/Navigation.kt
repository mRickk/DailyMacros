package com.example.dailymacros.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dailymacros.ui.screens.addexercise.AddExerciseScreen
import com.example.dailymacros.ui.screens.addexercise.AddExerciseViewModel
import com.example.dailymacros.ui.screens.diary.DiaryScreen
import com.example.dailymacros.ui.screens.diary.DiaryViewModel
import com.example.dailymacros.ui.screens.diet.Diet
import com.example.dailymacros.ui.screens.editprofile.EditProfile
import com.example.dailymacros.ui.screens.editprofile.EditProfileViewModel
import com.example.dailymacros.ui.screens.login.Login
import com.example.dailymacros.ui.screens.login.LoginViewModel
import com.example.dailymacros.ui.screens.overview.Overview
import com.example.dailymacros.ui.screens.profile.Profile
import com.example.dailymacros.ui.screens.profile.ProfileViewModel
import com.example.dailymacros.ui.screens.search.Search
import com.example.dailymacros.ui.screens.selectexercise.SelectExerciseScreen
import com.example.dailymacros.ui.screens.selectexercise.SelectExerciseViewModel
import com.example.dailymacros.ui.screens.settings.Settings
import com.example.dailymacros.ui.screens.settings.SettingsViewModel
import com.example.dailymacros.ui.screens.signup.Signup
import com.example.dailymacros.ui.screens.signup.SignupViewModel
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
            Login(navController, loginViewModel)
        }
        composable(NavigationRoute.Signin.route) {
            val signupViewModel = koinViewModel<SignupViewModel>()
            Signup(navController, signupViewModel, signupViewModel.userState, signupViewModel.actions)
        }
        composable(NavigationRoute.Diary.route) {
            DiaryScreen(navController,
                koinViewModel<DiaryViewModel>().actions,
                koinViewModel<DiaryViewModel>().state.collectAsStateWithLifecycle().value)
        }
        composable(NavigationRoute.Search.route) {
            Search(navController)
        }
        composable(NavigationRoute.Profile.route) {
            val profileViewModel = koinViewModel<ProfileViewModel>()
            Profile(navController, profileViewModel)
        }
        composable(NavigationRoute.EditProfile.route) {
            val editProfileViewModel = koinViewModel<EditProfileViewModel>()
            EditProfile(navController, editProfileViewModel)
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
        composable(NavigationRoute.SelectExercise.route + "?date={date}") { backStackEntry ->
            val selectExerciseVM = koinViewModel<SelectExerciseViewModel>()
            val state by selectExerciseVM.state.collectAsStateWithLifecycle()
            SelectExerciseScreen(navController, selectExerciseVM, state, backStackEntry.arguments?.getString("date"))
        }
        composable(NavigationRoute.AddExercise.route + "?exerciseName={exerciseName}") {backStackEntry ->
            AddExerciseScreen(navController,
                koinViewModel<AddExerciseViewModel>(),
                backStackEntry.arguments?.getString("exerciseName"))
        }
        // AddFood inside SelectFood screen?
        // SelectAll and Add singular items?

        composable(NavigationRoute.AddFood.route) {
            //AddFood(navController)
        }


        composable(NavigationRoute.AddRecipe.route) {
            //AddRecipe(navController)
        }
        composable(NavigationRoute.SelectRecipe.route) {
            //SelectRecipe(navController)
        }

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
    data object EditProfile : NavigationRoute("EditProfile")
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