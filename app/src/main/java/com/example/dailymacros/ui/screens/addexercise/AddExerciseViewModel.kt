package com.example.dailymacros.ui.screens.addexercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.database.Exercise
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

data class AddExerciseState(val exercise: Exercise?)

interface AddExerciseActions {
    fun upsertExercise(exercise: Exercise) : Job
    fun getExercise(name: String) : Job
}
class AddExerciseViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository
) : ViewModel() {
    var state = AddExerciseState(null)

    val actions = object : AddExerciseActions {
        override fun upsertExercise(exercise: Exercise) = viewModelScope.launch {
            dailyMacrosRepository.upsertExercise(exercise)
        }
        override fun getExercise(name: String) = viewModelScope.launch {
            val exercise = dailyMacrosRepository.getExercise(name)
            state = AddExerciseState(exercise)
        }
    }
}