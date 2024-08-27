package com.example.dailymacros.ui.screens.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.dailymacros.data.database.User
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import com.example.dailymacros.data.repositories.DatastoreRepository
import com.example.dailymacros.ui.NavigationRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class UserState(val user: User?)

interface ProfileActions {
    fun setProfilePicUrl(email: String, profilePicUrl: String): Job
    fun logout(navController: NavHostController): Job
}

class ProfileViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository,
    private val datastoreRepository: DatastoreRepository) : ViewModel() {
    var loggedUser by mutableStateOf(UserState(null))
        private set

    val actions = object : ProfileActions {
        override fun setProfilePicUrl(email: String, profilePicUrl: String) =
            viewModelScope.launch {
                dailyMacrosRepository.setProfilePicUrl(email, profilePicUrl)
                if (loggedUser.user != null) {
                    val userCopy = User(
                        email = loggedUser.user!!.email,
                        password = loggedUser.user!!.password,
                        username = loggedUser.user!!.username,
                        pictureUrl = profilePicUrl,
                        height = loggedUser.user!!.height,
                        weight = loggedUser.user!!.weight,
                        gender = loggedUser.user!!.gender,
                        age = loggedUser.user!!.age,
                        activity = loggedUser.user!!.activity,
                        goal = loggedUser.user!!.goal,
                        bmr = loggedUser.user!!.bmr,
                        dailyKcal = loggedUser.user!!.dailyKcal,
                        diet = loggedUser.user!!.diet
                    )
                    datastoreRepository.saveUser(userCopy)
                    loggedUser = UserState(userCopy)
                }
            }

        override fun logout(navController: NavHostController): Job = viewModelScope.launch {
            viewModelScope.launch {
                datastoreRepository.removeUser()
                loggedUser = UserState(null)
            }.join()
            navController.navigate(NavigationRoute.Login.route)
        }
    }
    init {
        viewModelScope.launch {
            loggedUser = UserState(datastoreRepository.user.first())
        }
    }
}