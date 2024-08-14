package com.example.dailymacros.ui.screens.addexercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.database.Exercise
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

interface AddExerciseActions {
    fun upsertExercise(exercise: Exercise) : Job
}
class AddExerciseViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository
) : ViewModel() {
    val actions = object : AddExerciseActions {
        override fun upsertExercise(exercise: Exercise) = viewModelScope.launch {
            dailyMacrosRepository.upsertExercise(exercise)
        }
    }
}