package com.example.dailymacros.ui.screens.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.database.Exercise
import com.example.dailymacros.data.database.ExerciseInsideDay
import com.example.dailymacros.data.database.ExerciseInsideDayWithExercise
import com.example.dailymacros.data.database.Food
import com.example.dailymacros.data.database.FoodInsideMeal
import com.example.dailymacros.data.database.FoodInsideMealWithFood
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DiaryState(
    val foodInsideAllMeals: MutableList<FoodInsideMealWithFood>,
    val exercisesInsideAllDays: MutableList<ExerciseInsideDayWithExercise>
)

interface DiaryActions {
    fun upsertFoodInsideMeal(fim: FoodInsideMeal): Job
    fun removeFoodInsideMeal(fim: FoodInsideMeal): Job

    fun upsertExerciseInsideDay(eid: ExerciseInsideDay): Job
    fun removeExerciseInsideDay(eid: ExerciseInsideDay): Job

}

class DiaryViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository
) : ViewModel() {

    val state = combine(
        dailyMacrosRepository.foodInsideAllMeals,
        dailyMacrosRepository.exercisesInsideAllDays
    ) { foodInsideAllMeals, exercisesInsideAllDays ->

        DiaryState(foodInsideAllMeals.toMutableList(), exercisesInsideAllDays.toMutableList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = DiaryState(mutableListOf(), mutableListOf())
    )

    val actions = object : DiaryActions {

        override fun upsertFoodInsideMeal(fim: FoodInsideMeal) = viewModelScope.launch {
            val foodInsideMeal = FoodInsideMeal(fim.foodName, fim.date, fim.mealType, fim.quantity)
            dailyMacrosRepository.upsertFoodInsideMeal(foodInsideMeal)
        }
        override fun removeFoodInsideMeal(fim: FoodInsideMeal) = viewModelScope.launch {
            dailyMacrosRepository.deleteFoodsInsideMeal(fim.date, fim.mealType, fim.foodName)
        }
        override fun upsertExerciseInsideDay(eid: ExerciseInsideDay) = viewModelScope.launch {
            dailyMacrosRepository.upsertExerciseInsideDay(eid)
        }
        override fun removeExerciseInsideDay(eid: ExerciseInsideDay) = viewModelScope.launch {
            dailyMacrosRepository.deleteExerciseInsideDay(eid)
        }
    }
}

