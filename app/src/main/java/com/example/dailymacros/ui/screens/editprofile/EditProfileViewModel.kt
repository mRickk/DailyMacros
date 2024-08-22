package com.example.dailymacros.ui.screens.editprofile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.database.Gender
import com.example.dailymacros.data.database.User
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import com.example.dailymacros.data.repositories.DatastoreRepository
import com.example.dailymacros.ui.screens.login.LoginActions
import com.example.dailymacros.ui.screens.profile.UserState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


interface EditProfileActions {
    fun updateProfile(weight : Float, height: Float, age: Int, gender: Gender): Job
}

class EditProfileViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {
    var loggedUser by mutableStateOf(UserState(null))
        private set

    val actions = object : EditProfileActions {
        override fun updateProfile(weight: Float, height: Float, age: Int, gender: Gender) =
            viewModelScope.launch {
                dailyMacrosRepository.updateAge(loggedUser.user!!.email, age)
                dailyMacrosRepository.updateHeight(loggedUser.user!!.email, height)
                dailyMacrosRepository.updateWeight(loggedUser.user!!.email, weight)
                dailyMacrosRepository.updateGender(loggedUser.user!!.gender.string, gender)
                if (loggedUser.user != null) {
                    val userCopy = User(
                        email = loggedUser.user!!.email,
                        password = loggedUser.user!!.password,
                        username = loggedUser.user!!.username,
                        pictureUrl = loggedUser.user!!.pictureUrl,
                        height = height,
                        weight = weight,
                        gender = gender,
                        age = age,
                        activity = loggedUser.user!!.activity,
                        goal = loggedUser.user!!.goal,
                        bmr = loggedUser.user!!.bmr,
                        dailyKcal = loggedUser.user!!.dailyKcal
                    )
                    datastoreRepository.saveUser(userCopy)
                    loggedUser = UserState(userCopy)
                }
            }

        init {
            viewModelScope.launch {
                loggedUser = UserState(datastoreRepository.user.first())
                Log.v("EditProfileVM", "Sono init: ${loggedUser.user}")
            }
        }
    }
}