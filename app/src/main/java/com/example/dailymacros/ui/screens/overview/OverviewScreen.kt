package com.example.dailymacros.ui.screens.overview

import android.text.format.DateUtils
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dailymacros.data.database.Exercise
import com.example.dailymacros.data.database.ExerciseInsideDay
import com.example.dailymacros.data.database.ExerciseInsideDayWithExercise
import com.example.dailymacros.data.database.Food
import com.example.dailymacros.data.database.FoodInsideMeal
import com.example.dailymacros.data.database.FoodInsideMealWithFood
import com.example.dailymacros.data.database.FoodUnit
import com.example.dailymacros.data.database.MealType
import com.example.dailymacros.ui.composables.BarChart
import com.example.dailymacros.ui.composables.DMTopAppBar
import com.example.dailymacros.ui.composables.NavBar
import com.example.dailymacros.ui.composables.StackedBarChart
import com.example.dailymacros.ui.theme.Carbs
import com.example.dailymacros.ui.theme.Fat
import com.example.dailymacros.ui.theme.Protein
import com.example.dailymacros.utilities.OverviewPeriods
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.SortedMap
import kotlin.math.roundToInt

@Composable
fun Overview(
    navController: NavHostController,
    state: OverviewState
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedPeriod by remember { mutableStateOf(OverviewPeriods.WEEK) }
    val startOfDayMillis = LocalDate.now()
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli()

    Scaffold (
        topBar = { DMTopAppBar(navController) },
        bottomBar = { NavBar(navController, selectedIndex = 2) }
    ){paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .align(Alignment.CenterHorizontally)) {
                OutlinedTextField(
                    value = selectedPeriod.string,
                    onValueChange = {},
                    label = { Text("Period") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) {
                    OverviewPeriods.entries.forEach { period ->
                        DropdownMenuItem(
                            onClick = {
                                selectedPeriod = period
                                expanded = false
                            },
                            text = {Text(text = period.string)}
                        )
                    }
                }
            }


            val foodInsideAllMeals = state.foodInsideAllMeals
            val exercisesInsideAllDays = state.exercisesInsideAllDays

            val daysInRange = generateSequence(startOfDayMillis) { it - DateUtils.DAY_IN_MILLIS }
                .takeWhile { it >= startOfDayMillis - selectedPeriod.periodInMillis + 1 }
                .toList()

            val mealsGroupedByDate = daysInRange.associateWith { day ->
                foodInsideAllMeals.filter { it.foodInsideMeal.date.toLong() == day }
                    .ifEmpty { listOf(createEmptyFoodInsideMealWithFood(day)) }
            }.toSortedMap()

            val exercisesGroupedByDate = daysInRange.associateWith { day ->
                exercisesInsideAllDays.filter { it.exerciseInsideDay.date.toLong() == day }
                    .ifEmpty { listOf(createEmptyExerciseInsideDay(day)) }
            }.toSortedMap()

            Text(
                text = "Calories and macros you ate",
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontWeight = MaterialTheme.typography.headlineSmall.fontWeight,
                modifier = Modifier.padding(8.dp)
            )
            StackedBarChart(groupedMeals = mealsGroupedByDate)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Calories you burned",
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontWeight = MaterialTheme.typography.headlineSmall.fontWeight,
                modifier = Modifier.padding(8.dp)
            )
            BarChart(
                values = exercisesGroupedByDate.values.map {it.sumOf { i -> (i.exercise.kcalBurnedSec * i.exerciseInsideDay.duration).toDouble() }.toFloat() },
                modifier = Modifier.padding(10.dp)
            )

        }
    }
}

fun createEmptyFoodInsideMealWithFood(date: Long): FoodInsideMealWithFood {
    return FoodInsideMealWithFood(
        foodInsideMeal = FoodInsideMeal(
            emailFIM = "",
            foodName = "",
            date = date.toString(),
            mealType = MealType.BREAKFAST,
            quantity = 0f
        ),
        food = Food(
            email = "",
            name = "",
            description = null,
            kcalPerc = 0f,
            carbsPerc = 0f,
            fatPerc = 0f,
            proteinPerc = 0f,
            unit = FoodUnit.GRAMS,
            isFavourite = false
        )
    )
}

fun createEmptyExerciseInsideDay(date: Long): ExerciseInsideDayWithExercise {
    return ExerciseInsideDayWithExercise(
        exerciseInsideDay = ExerciseInsideDay(
            emailEID = "",
            date = date.toString(),
            duration = 0,
            exerciseName = ""
        ),
        exercise = Exercise(
            email = "",
            name = "",
            kcalBurnedSec = 0f,
            description = null,
            isFavourite = false
        )
    )
}