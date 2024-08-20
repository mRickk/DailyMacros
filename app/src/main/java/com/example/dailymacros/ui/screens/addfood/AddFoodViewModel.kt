package com.example.dailymacros.ui.screens.addfood

import com.example.dailymacros.data.database.Food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

data class AddFoodState(val food: Food?)

interface AddFoodActions {
    fun upsertFood(food: Food) : Job
    fun getFood(name: String) : Job
}
class AddFoodViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository
) : ViewModel() {
    var state = AddFoodState(null)

    val actions = object : AddFoodActions {
        override fun upsertFood(food: Food) = viewModelScope.launch {
            dailyMacrosRepository.upsertFood(food)
        }
        override fun getFood(name: String) = viewModelScope.launch {
            val exercise = dailyMacrosRepository.getFood(name)
            state = AddFoodState(exercise)
        }
    }
}