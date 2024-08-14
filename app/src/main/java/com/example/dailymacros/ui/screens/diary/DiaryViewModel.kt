package com.example.dailymacros.ui.screens.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.database.Exercise
import com.example.dailymacros.data.database.Food
import com.example.dailymacros.data.database.FoodInsideMeal
import com.example.dailymacros.data.database.Meal
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

data class DiaryState(val diaryPair: Pair<MutableMap<Meal, List<Pair<Food,Float>>>, MutableList<Pair<Exercise, Int>>>)

interface DiaryActions {
    fun getMealsMap(date: String): Job
    fun insertFood(food: Food, meal: Meal, quantity: Float): Job
    fun removeFood(food: Food, meal: Meal): Job
}

class DiaryViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository
) : ViewModel() {
    private val _state = MutableStateFlow(DiaryState(Pair(emptyMap<Meal, List<Pair<Food,Float>>>().toMutableMap(), emptyList<Pair<Exercise,Int>>().toMutableList())))
    val state = _state.asStateFlow()
    fun changeState(newState: DiaryState) {
        _state.value = newState
    }

    val actions = object : DiaryActions {
        override fun getMealsMap(date: String) = viewModelScope.launch {
            val mealsList = dailyMacrosRepository.getMeals(date).toList().flatten()
            val first = emptyMap<Meal, List<Pair<Food,Float>>>().toMutableMap()
            for (meal in mealsList) {
                first[meal] =  dailyMacrosRepository.getFoodsInsideMeal(meal).toList().flatten().map { Pair(dailyMacrosRepository.getFood(it.foodName), it.quantity) }
            }
            val second = dailyMacrosRepository.getExercisesInsideDay(date).toList().flatten().map { Pair(dailyMacrosRepository.getExercise(it.exerciseName), it.duration) }.toMutableList()
            changeState(DiaryState(Pair(first, second)))
        }
        override fun insertFood(food: Food, meal: Meal, quantity: Float) = viewModelScope.launch {
            val foodInsideMeal = FoodInsideMeal(food.name, meal.date, meal.type, quantity)
            dailyMacrosRepository.upsertFoodInsideMeal(foodInsideMeal)
            dailyMacrosRepository.upsertMeal(meal)
        }
        override fun removeFood(food: Food, meal: Meal) = viewModelScope.launch {
            dailyMacrosRepository.deleteFoodsInsideMeal(meal, food)
            if (dailyMacrosRepository.getFoodsInsideMeal(meal).count() == 0) {
                dailyMacrosRepository.deleteMeal(meal.date, meal.type)
            } else {
                dailyMacrosRepository.upsertMeal(meal) //TODO: check if works without updating before deleting
            }
        }
    }
}

