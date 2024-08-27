package com.example.dailymacros.ui.screens.addfood

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.dailymacros.data.database.Food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.database.FoodUnit
import com.example.dailymacros.data.database.User
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import com.example.dailymacros.data.repositories.DatastoreRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class UserState(val user: User?)
data class AddFoodState(val food: Food?)

interface AddFoodActions {
    fun upsertFood(name: String, description:String?, kcalPerc: Float, carbsPerc: Float, fatPerc: Float, proteinPerc: Float, unit: FoodUnit) : Job
    fun getFood(name: String) : Job
}
class AddFoodViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {
    var state = AddFoodState(null)
    var loggedUser by mutableStateOf(UserState(null))
        private set

    val actions = object : AddFoodActions {
        override fun upsertFood(name: String, description:String?, kcalPerc: Float, carbsPerc: Float, fatPerc: Float, proteinPerc: Float, unit: FoodUnit) = viewModelScope.launch {
            dailyMacrosRepository.upsertFood(Food(loggedUser.user?.email ?: "", name, description, kcalPerc, carbsPerc, fatPerc, proteinPerc, unit, false))
        }
        override fun getFood(name: String) = viewModelScope.launch {
            val exercise = dailyMacrosRepository.getFood(name, loggedUser.user?.email ?: "")
            state = AddFoodState(exercise)
        }
    }

    init {
        viewModelScope.launch {
            loggedUser = UserState(datastoreRepository.user.first())
        }
    }
}