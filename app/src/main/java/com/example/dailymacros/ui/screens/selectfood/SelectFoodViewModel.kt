package com.example.dailymacros.ui.screens.selectfood

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.database.Food
import com.example.dailymacros.data.database.FoodInsideMeal
import com.example.dailymacros.data.database.Gender
import com.example.dailymacros.data.database.MealType
import com.example.dailymacros.data.database.User
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import com.example.dailymacros.data.repositories.DatastoreRepository
import com.example.dailymacros.ui.screens.selectexercise.UserState
import com.example.dailymacros.utilities.calculateBMR
import com.example.dailymacros.utilities.calculateDailyKcal
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SelectFoodState(val foodList: List<Food>)
data class UserState(val user: User?)

interface SelectFoodActions {
    fun insertFoodInsideMeal(food : Food, date : String, mealType: MealType, quantity: Float): Job
    fun deleteFood(food: Food): Job
    fun toggleFavourite(food: Food): Job
    fun updateUser(user: User): Job
}

class SelectFoodViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {
    val state = dailyMacrosRepository.foods.map { SelectFoodState(it.filter {i ->
        i.email == (loggedUser.user?.email ?: "")
    }) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SelectFoodState(emptyList())
    )
    var loggedUser by mutableStateOf(UserState(null))
        private set

    @OptIn(ExperimentalCoroutinesApi::class)
    val actions = object : SelectFoodActions {
        override fun insertFoodInsideMeal(food : Food, date : String, mealType: MealType, quantity: Float) = viewModelScope.launch {
            dailyMacrosRepository.upsertFoodInsideMeal(FoodInsideMeal(loggedUser.user?.email ?: "", food.name, date, mealType, quantity))
        }
        override fun deleteFood(food: Food) = viewModelScope.launch {
            dailyMacrosRepository.deleteFood(food.name, loggedUser.user?.email ?: "")
        }

        override fun toggleFavourite(food: Food) = viewModelScope.launch {
            dailyMacrosRepository.upsertFood(food)
        }
        override fun updateUser(user: User) = viewModelScope.launch {
            if (loggedUser.user != null) {
                val userCopy = user.copy()
                dailyMacrosRepository.updateUser(userCopy)
                datastoreRepository.saveUser(userCopy)
                loggedUser = UserState(userCopy)
            }
        }
    }

    init {
        viewModelScope.launch {
            loggedUser = UserState(datastoreRepository.user.first())
        }
    }
}