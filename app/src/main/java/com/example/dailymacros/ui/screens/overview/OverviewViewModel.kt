package com.example.dailymacros.ui.screens.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.database.ExerciseInsideDayWithExercise
import com.example.dailymacros.data.database.FoodInsideMealWithFood
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class OverviewState(
    val foodInsideAllMeals: MutableList<FoodInsideMealWithFood>,
    val exercisesInsideAllDays: MutableList<ExerciseInsideDayWithExercise>
)

class OverviewViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository
) : ViewModel() {

    val state = combine(
        dailyMacrosRepository.foodInsideAllMeals,
        dailyMacrosRepository.exercisesInsideAllDays
    ) { foodInsideAllMeals, exercisesInsideAllDays ->
        OverviewState(foodInsideAllMeals.toMutableList(), exercisesInsideAllDays.toMutableList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = OverviewState(mutableListOf(), mutableListOf())
    )
}
