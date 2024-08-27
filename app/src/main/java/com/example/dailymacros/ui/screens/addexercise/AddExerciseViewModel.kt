package com.example.dailymacros.ui.screens.addexercise

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.database.Exercise
import com.example.dailymacros.data.database.User
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import com.example.dailymacros.data.repositories.DatastoreRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class UserState(val user: User?)
data class AddExerciseState(val exercise: Exercise?)

interface AddExerciseActions {
    fun upsertExercise(name: String, description:String?, kcalBurnedSec:Float) : Job
    fun getExercise(name: String) : Job
}
class AddExerciseViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {
    var state = AddExerciseState(null)
    var loggedUser by mutableStateOf(UserState(null))
        private set

    val actions = object : AddExerciseActions {
        override fun upsertExercise(name: String, description:String?, kcalBurnedSec:Float) = viewModelScope.launch {
            dailyMacrosRepository.upsertExercise(Exercise(loggedUser.user?.email ?: "", name, description, kcalBurnedSec, false))
        }
        override fun getExercise(name: String) = viewModelScope.launch {
            val exercise = dailyMacrosRepository.getExercise(name, loggedUser.user?.email ?: "")
            state = AddExerciseState(exercise)
        }
    }

    init {
        viewModelScope.launch {
            loggedUser = UserState(datastoreRepository.user.first())
        }
    }
}