package com.example.dailymacros.ui.screens.editprofile

import android.provider.ContactsContract.CommonDataKinds.Email
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
import com.example.dailymacros.utilities.calculateBMR
import com.example.dailymacros.utilities.calculateDailyKcal
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


interface EditProfileActions {
    fun updateProfile(username: String, weight : Float, height: Float, age: Int, gender: Gender): Job
    fun getUser(): Job
}

class EditProfileViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {
    var loggedUser by mutableStateOf(UserState(null))
        private set

    val actions = object : EditProfileActions {
        override fun updateProfile(username: String ,weight: Float, height: Float, age: Int, gender: Gender) =
            viewModelScope.launch {
                if (loggedUser.user != null) {
                    val tempBMR = calculateBMR(height, weight, age, gender.k)
                    val userCopy = User(
                        email = loggedUser.user!!.email,
                        password = loggedUser.user!!.password,
                        username = username,
                        pictureUrl = loggedUser.user!!.pictureUrl,
                        height = height,
                        weight = weight,
                        gender = gender,
                        age = age,
                        activity = loggedUser.user!!.activity,
                        goal = loggedUser.user!!.goal,
                        bmr = tempBMR,
                        dailyKcal = calculateDailyKcal(tempBMR, loggedUser.user!!.activity.k, loggedUser.user!!.goal.k),
                        diet = loggedUser.user!!.diet,
                        b1 = loggedUser.user!!.b1,
                        b2 = loggedUser.user!!.b2,
                        b3 = loggedUser.user!!.b3,
                        b4 = loggedUser.user!!.b4,
                        b5 = loggedUser.user!!.b5,
                        b6 = loggedUser.user!!.b6
                    )
                    dailyMacrosRepository.updateUser(userCopy)
                    datastoreRepository.saveUser(userCopy)
                    loggedUser = UserState(userCopy)
                }
            }

        override fun getUser() = viewModelScope.launch {
            loggedUser = UserState(datastoreRepository.user.first())
        }

        init {
            viewModelScope.launch {
                loggedUser = UserState(datastoreRepository.user.first())
            }
        }
    }
}