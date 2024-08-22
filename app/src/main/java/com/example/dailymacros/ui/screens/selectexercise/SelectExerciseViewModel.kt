package com.example.dailymacros.ui.screens.selectexercise

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.database.Exercise
import com.example.dailymacros.data.database.ExerciseInsideDay
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

data class SelectExerciseState(val exerciseList: List<Exercise>)

interface SelectExerciseActions {
    fun insertExerciseInsideDay(id: Long?, exercise: Exercise, date:String, duration: Int): Job
    fun deleteExercise(exercise: Exercise): Job
}

class SelectExerciseViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository
) : ViewModel() {
    val state = dailyMacrosRepository.exercises.map { SelectExerciseState(it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SelectExerciseState(emptyList())
    )

    val actions = object : SelectExerciseActions {
        override fun insertExerciseInsideDay(id: Long?, exercise: Exercise, date:String, duration: Int) = viewModelScope.launch {
            dailyMacrosRepository.upsertExerciseInsideDay(ExerciseInsideDay(exercise.name, date, duration))
        }
        override fun deleteExercise(exercise: Exercise) = viewModelScope.launch {
            dailyMacrosRepository.deleteExercise(exercise.name)
        }
    }
}