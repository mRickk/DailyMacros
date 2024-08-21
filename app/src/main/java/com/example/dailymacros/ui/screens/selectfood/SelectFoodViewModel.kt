package com.example.dailymacros.ui.screens.selectfood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.database.Exercise
import com.example.dailymacros.data.database.ExerciseInsideDay
import com.example.dailymacros.data.database.Food
import com.example.dailymacros.data.database.FoodInsideMeal
import com.example.dailymacros.data.database.MealType
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SelectFoodState(val foodList: List<Food>)

interface SelectFoodActions {
    fun insertFoodInsideMeal(food : Food, date : String, mealType: MealType, quantity: Float): Job
    fun deleteFood(food: Food): Job
}

class SelectFoodViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository
) : ViewModel() {
    val state = dailyMacrosRepository.foods.map { SelectFoodState(it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SelectFoodState(emptyList())
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val actions = object : SelectFoodActions {
        override fun insertFoodInsideMeal(food : Food, date : String, mealType: MealType, quantity: Float) = viewModelScope.launch {
            dailyMacrosRepository.upsertFoodInsideMeal(FoodInsideMeal(food.name, date, mealType, quantity))
        }
        override fun deleteFood(food: Food) = viewModelScope.launch {
            dailyMacrosRepository.deleteFood(food.name)
        }
    }
}