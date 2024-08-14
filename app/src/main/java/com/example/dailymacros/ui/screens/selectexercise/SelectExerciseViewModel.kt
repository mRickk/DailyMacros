package com.example.dailymacros.ui.screens.selectexercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.database.Exercise
import com.example.dailymacros.data.database.ExerciseInsideDay
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

data class SelectExerciseState(val exerciseList: List<Exercise>)

interface SelectExerciseActions {
    fun getExerciseList(): Job
    fun insertExerciseInsideDay(id: Long?, exercise: Exercise, date:String, duration: Int): Job
    fun deleteExercise(exercise: Exercise): Job
}

class SelectExerciseViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SelectExerciseState(emptyList()))
    val state = _state.asStateFlow()
    fun changeState(newState: SelectExerciseState) {
        _state.value = newState
    }

    val actions = object : SelectExerciseActions {
        override fun getExerciseList() = viewModelScope.launch {
            val selectTabList = dailyMacrosRepository.exercises.toList().flatten()
            changeState(SelectExerciseState(selectTabList))
        }
        override fun insertExerciseInsideDay(id: Long?, exercise: Exercise, date:String, duration: Int) = viewModelScope.launch {
            dailyMacrosRepository.upsertExerciseInsideDay(ExerciseInsideDay(id?:0L, exercise.name, date, duration))
        }
        override fun deleteExercise(exercise: Exercise) = viewModelScope.launch {
            dailyMacrosRepository.deleteSport(exercise.name)
        }
    }
}