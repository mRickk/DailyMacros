package com.example.dailymacros.ui.screens.diet

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.database.ActivityType
import com.example.dailymacros.data.database.DietType
import com.example.dailymacros.data.database.GoalType
import com.example.dailymacros.data.database.User
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import com.example.dailymacros.data.repositories.DatastoreRepository
import com.example.dailymacros.utilities.calculateDailyKcal
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class UserState(val user: User?)

interface DietActions {
    fun getUser(): Job
    fun updateUser(dietType: DietType, activityType: ActivityType,goalType: GoalType): Job
}


class DietViewModel (
    private val dailyMacrosRepository: DailyMacrosRepository,
    private val datastoreRepository: DatastoreRepository
): ViewModel() {
    var loggedUser by mutableStateOf(UserState(null))
        private set

    val actions = object : DietActions {
        override fun getUser() = viewModelScope.launch {
            loggedUser = UserState(datastoreRepository.user.first())
            Log.v("DietScreen", "GETUSER: ${loggedUser.user}")
        }

        override fun updateUser(dietType: DietType, activityType: ActivityType, goalType: GoalType) = viewModelScope.launch {
            if (loggedUser.user != null) {
                val userCopy = User(
                    email = loggedUser.user!!.email,
                    password = loggedUser.user!!.password,
                    username = loggedUser.user!!.username,
                    pictureUrl = loggedUser.user!!.pictureUrl,
                    height = loggedUser.user!!.height,
                    weight = loggedUser.user!!.weight,
                    gender = loggedUser.user!!.gender,
                    age = loggedUser.user!!.age,
                    activity = activityType,
                    goal = goalType,
                    bmr = loggedUser.user!!.bmr,
                    dailyKcal = calculateDailyKcal(loggedUser.user!!.bmr, activityType.k, goalType.k),
                    diet = dietType,
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
    }

    init {
        viewModelScope.launch {
            loggedUser = UserState(datastoreRepository.user.first())
        }
    }

}