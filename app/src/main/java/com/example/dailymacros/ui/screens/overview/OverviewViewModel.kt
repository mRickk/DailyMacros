package com.example.dailymacros.ui.screens.overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymacros.data.database.ExerciseInsideDayWithExercise
import com.example.dailymacros.data.database.FoodInsideMealWithFood
import com.example.dailymacros.data.database.User
import com.example.dailymacros.data.repositories.DailyMacrosRepository
import com.example.dailymacros.data.repositories.DatastoreRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class OverviewState(
    val foodInsideAllMeals: MutableList<FoodInsideMealWithFood>,
    val exercisesInsideAllDays: MutableList<ExerciseInsideDayWithExercise>
)

data class UserState(val user: User?)

class OverviewViewModel(
    private val dailyMacrosRepository: DailyMacrosRepository,
    private val datastoreRepository: DatastoreRepository
) : ViewModel() {

    val state = combine(
        dailyMacrosRepository.foodInsideAllMeals,
        dailyMacrosRepository.exercisesInsideAllDays
    ) { foodInsideAllMeals, exercisesInsideAllDays ->
        OverviewState(foodInsideAllMeals.filter { it.foodInsideMeal.emailFIM == (loggedUser.user?.email ?: "") }.toMutableList(),
            exercisesInsideAllDays.filter { it.exerciseInsideDay.emailEID == (loggedUser.user?.email ?: "") }.toMutableList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = OverviewState(mutableListOf(), mutableListOf())
    )
    var loggedUser by mutableStateOf(UserState(null))
        private set

    init {
        viewModelScope.launch {
            loggedUser = UserState(datastoreRepository.user.first())
        }
    }
}
