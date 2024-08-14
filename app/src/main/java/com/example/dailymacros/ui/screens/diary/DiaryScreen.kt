package com.example.dailymacros.ui.screens.diary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dailymacros.data.database.Meal
import com.example.dailymacros.data.database.MealType
import com.example.dailymacros.ui.composables.DMTopAppBar
import com.example.dailymacros.ui.composables.ExerciseInfo
import com.example.dailymacros.ui.composables.ExerciseInfoData
import com.example.dailymacros.ui.composables.FoodInfoData
import com.example.dailymacros.ui.composables.MealInfo
import com.example.dailymacros.ui.composables.datePickerWithDialog
import com.example.dailymacros.ui.theme.Cal
import com.example.dailymacros.ui.theme.Carbs
import com.example.dailymacros.ui.theme.Fat
import com.example.dailymacros.ui.theme.Protein
import com.example.dailymacros.utilities.MacrosKcal
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryScreen(
    navController: NavHostController,
    actions: DiaryActions,
    state: DiaryState
) {
    var dateState : DatePickerState? = null
    Scaffold(
        topBar = { DMTopAppBar(navController) }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()
            .fillMaxHeight()
        ) {
            Row() {
                dateState = datePickerWithDialog()
            }

            if (dateState == null) {
                Text("Please select a date.")
            } else {
                val dateStamp = dateState!!.selectedDateMillis.toString()
                actions.getMealsMap(dateStamp)
                val diaryPair = state.diaryPair

                val mealTypes = MealType.entries.toMutableList()
                for (meal in diaryPair.first.keys) {
                    if (mealTypes.contains(meal.type)) {
                        mealTypes.remove(meal.type)
                    }
                }
                mealTypes.forEach { mT ->
                    diaryPair.first[Meal(mT, dateStamp)] = emptyList()
                }

                val allFoodList = diaryPair.first.values.flatten()
                val allExercisesList = diaryPair.second
                // Calculate total grams and kcal for each macronutrient
                val countCarbs = if (allFoodList.isNotEmpty()) allFoodList.sumOf { it.first.carbsPerc * it.second.toDouble() } else 0.0
                val countFat = if (allFoodList.isNotEmpty()) allFoodList.sumOf { it.first.fatPerc * it.second.toDouble() } else 0.0
                val countProtein = if (allFoodList.isNotEmpty()) allFoodList.sumOf { it.first.proteinPerc * it.second.toDouble() } else 0.0
                val countCarbsKcal = countCarbs * MacrosKcal.CARBS.kcal
                val countFatKcal = countFat * MacrosKcal.FAT.kcal
                val countProteinKcal = countProtein * MacrosKcal.PROTEIN.kcal
                val countExerciseKcal = if (allExercisesList.isNotEmpty()) allExercisesList.sumOf { it.first.kcalBurnedSec * it.second.toDouble() } else 0.0
                val countKcal = countCarbsKcal + countFatKcal + countProteinKcal - countExerciseKcal

                LazyColumn(
                        modifier = Modifier.fillMaxSize() // Fills the available space
                        ) {
                    item {
                        // Header section with calories and macros
                        Column(
                            modifier = Modifier.padding(8.dp) // Add padding to your content
                        ) {
                            CaloriesBar(countCal = countKcal, totCal = 2000f) // Example values

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                MacrosBar(
                                    label = "Carbs",
                                    count = countCarbs.toFloat(),
                                    total = 300f, //TODO: get from user
                                    color = Carbs
                                ) // Example values
                                MacrosBar(
                                    label = "Fat",
                                    count = countFat.toFloat(),
                                    total = 100f, //TODO: get from user
                                    color = Fat
                                ) // Example values
                                MacrosBar(
                                    label = "Protein",
                                    count = countProtein.toFloat(),
                                    total = 150f, //TODO: get from user
                                    color = Protein
                                ) // Example values
                            }
                        }
                    }

                    // Meals section
                    items(
                        diaryPair.first.toList()
                    ) { mealData ->
                        MealInfo(
                            meal = mealData.first.type.string,
                            foodInfoList = mealData.second.map { foodInsideMeal ->
                                FoodInfoData(
                                    food = foodInsideMeal.first.name,
                                    quantity = foodInsideMeal.second.toFloat(),
                                    carbsQty = 0f,
                                    fatQty = 0f,
                                    protQty = 0f
                                )
                            },
                            navController = navController
                        )
                    }

                    // Exercise section
                    item {
                        ExerciseInfo(
                            exerciseInfoList = allExercisesList.map { exerciseInsideDay ->
                                ExerciseInfoData(
                                    exercise = exerciseInsideDay.first.name,
                                    caloriesBurned = exerciseInsideDay.first.kcalBurnedSec * exerciseInsideDay.second,
                                    duration = exerciseInsideDay.second
                                )
                            },
                            navController = navController
                        )
                    }

                }
            }
        }
    }
}

data class MealInfoData(
    val meal: String,
    val kcal: Int,
    val foodInfoList: List<FoodInfoData>
)

@Composable
fun MacrosBar(label: String, count: Float, total: Float, color: Color, modifier: Modifier = Modifier) {
    val total = total.roundToInt()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val barWidth = screenWidth / 3 * 0.8f // Slightly less than 1/3 of the screen width
    val progress = (count / total).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .padding(horizontal = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$label: ${count}/${total} g",
            color = color,
            style = MaterialTheme.typography.bodyMedium
        )
        Box(
            modifier = Modifier
                .width(barWidth)
                .height(9.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                    .background(color)
            )
        }
    }
}

@Composable
fun CaloriesBar(countCal: Double, totCal: Float, modifier: Modifier = Modifier) {
    val progress = (countCal.toFloat() / totCal).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Calories: ${countCal.roundToInt()}/${totCal.roundToInt()}kcal",
            color = Cal,
            style = MaterialTheme.typography.bodyMedium
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                    .background(Cal)
            )
        }
    }
}