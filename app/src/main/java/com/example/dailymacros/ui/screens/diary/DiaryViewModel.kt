package com.example.dailymacros.ui.screens.diary

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.database.ExerciseInsideDay
import com.example.dailymacros.data.database.ExerciseInsideDayWithExercise
import com.example.dailymacros.data.database.FoodInsideMeal
import com.example.dailymacros.data.database.FoodInsideMealWithFood
import com.example.dailymacros.data.database.MealType
import com.example.dailymacros.data.database.User
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import com.example.dailymacros.data.repositories.DatastoreRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DiaryState(
    val foodInsideAllMeals: MutableList<FoodInsideMealWithFood>,
    val exercisesInsideAllDays: MutableList<ExerciseInsideDayWithExercise>
)
data class UserState(val user: User?)

interface DiaryActions {
    fun upsertFoodInsideMeal(fim: FoodInsideMeal): Job
    fun removeFoodInsideMeal(foodName: String, date: String, mealType: MealType): Job

    fun upsertExerciseInsideDay(eid: ExerciseInsideDay): Job
    fun removeExerciseInsideDay(exerciseName: String, date: String, duration: Int): Job

}

class DiaryViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {

    val state = combine(
        dailyMacrosRepository.foodInsideAllMeals,
        dailyMacrosRepository.exercisesInsideAllDays
    ) { foodInsideAllMeals, exercisesInsideAllDays ->

        DiaryState(foodInsideAllMeals.filter { it.foodInsideMeal.emailFIM == (loggedUser.user?.email ?: "") }.toMutableList(),
            exercisesInsideAllDays.filter { it.exerciseInsideDay.emailEID == (loggedUser.user?.email ?: "") }.toMutableList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = DiaryState(mutableListOf(), mutableListOf())
    )
    var loggedUser by mutableStateOf(UserState(null))
        private set

    val actions = object : DiaryActions {

        override fun upsertFoodInsideMeal(fim: FoodInsideMeal) = viewModelScope.launch {
            val foodInsideMeal = FoodInsideMeal(loggedUser.user?.email ?: "", fim.foodName, fim.date, fim.mealType, fim.quantity)
            dailyMacrosRepository.upsertFoodInsideMeal(foodInsideMeal)
        }
        override fun removeFoodInsideMeal(foodName: String, date: String, mealType: MealType) = viewModelScope.launch {
            dailyMacrosRepository.deleteFoodsInsideMeal(email=loggedUser.user?.email ?: "", foodName = foodName, date=date, mealType=mealType)
        }
        override fun upsertExerciseInsideDay(eid: ExerciseInsideDay) = viewModelScope.launch {
            dailyMacrosRepository.upsertExerciseInsideDay(eid)
        }
        override fun removeExerciseInsideDay(exerciseName: String, date: String, duration: Int) = viewModelScope.launch {
            dailyMacrosRepository.deleteExerciseInsideDay(ExerciseInsideDay(loggedUser.user?.email ?: "", exerciseName, date, duration))
        }
    }

    init {
        viewModelScope.launch {
            loggedUser = UserState(datastoreRepository.user.first())
        }
    }
}

