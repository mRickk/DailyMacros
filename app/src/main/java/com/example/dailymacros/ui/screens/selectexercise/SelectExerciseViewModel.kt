package com.example.dailymacros.ui.screens.selectexercise

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.database.Exercise
import com.example.dailymacros.data.database.ExerciseInsideDay
import com.example.dailymacros.data.database.User
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import com.example.dailymacros.data.repositories.DatastoreRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SelectExerciseState(val exerciseList: List<Exercise>)
data class UserState(val user: User?)

interface SelectExerciseActions {
    fun insertExerciseInsideDay(id: Long?, exercise: Exercise, date:String, duration: Int): Job
    fun deleteExercise(exercise: Exercise): Job
    fun toggleFavourite(exercise: Exercise): Job
}

class SelectExerciseViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {
    val state = dailyMacrosRepository.exercises.map { SelectExerciseState(it.filter {i ->
        i.email == (loggedUser.user?.email ?: "")
    }) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SelectExerciseState(emptyList())
    )

    var loggedUser by mutableStateOf(UserState(null))
        private set

    val actions = object : SelectExerciseActions {
        override fun insertExerciseInsideDay(id: Long?, exercise: Exercise, date:String, duration: Int) = viewModelScope.launch {
            dailyMacrosRepository.upsertExerciseInsideDay(ExerciseInsideDay(loggedUser.user?.email ?: "", exercise.name, date, duration))
        }
        override fun deleteExercise(exercise: Exercise) = viewModelScope.launch {
            dailyMacrosRepository.deleteExercise(exercise.name, loggedUser.user?.email ?: "")
        }
        override fun toggleFavourite(exercise: Exercise) = viewModelScope.launch {
            dailyMacrosRepository.upsertExercise(exercise)
        }
    }

    init {
        viewModelScope.launch {
            loggedUser = UserState(datastoreRepository.user.first())
        }
    }
}