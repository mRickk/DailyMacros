package com.example.dailymacros.ui.screens.diary

import android.text.format.DateUtils
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dailymacros.data.database.DietType
import com.example.dailymacros.data.database.MealType
import com.example.dailymacros.ui.composables.DMTopAppBar
import com.example.dailymacros.ui.composables.ExerciseInfo
import com.example.dailymacros.ui.composables.ExerciseInfoData
import com.example.dailymacros.ui.composables.FoodInfoData
import com.example.dailymacros.ui.composables.MealInfo
import com.example.dailymacros.ui.composables.NavBar
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
    state: DiaryState,
    diaryViewModel: DiaryViewModel
) {
    val context = LocalContext.current

    Scaffold(
        topBar = { DMTopAppBar(navController) },
        bottomBar = { NavBar(navController, selectedIndex = 0) }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()
            .fillMaxHeight()
        ) {
            Log.v("DiaryScreen", "selectedDateMillis: ${diaryViewModel.loggedUser.user?.selectedDateMillis}")
            val selectedDateMillis = remember { mutableStateOf(diaryViewModel.loggedUser.user?.selectedDateMillis) }
            Row() {
                selectedDateMillis.value = datePickerWithDialog(viewModel = diaryViewModel)
            }

            if (selectedDateMillis.value == null) {
                Text("Please select a date.")
            } else {
                Log.v("DiaryScreen", selectedDateMillis.value.toString())
                val dateStamp = selectedDateMillis.value.toString()

                val foodInsideDate = state.foodInsideAllMeals.filter { it.foodInsideMeal.date == dateStamp }
                val exercisesInsideDate = state.exercisesInsideAllDays.filter { it.exerciseInsideDay.date == dateStamp }

                // Organize food data by meal type
                val foodInsideMealsDate = MealType.entries.associateWith { meal ->
                    foodInsideDate.filter { it.foodInsideMeal.mealType == meal }
                }

                // Calculate total grams and kcal for each macronutrient
                val countCarbs = if (foodInsideDate.isNotEmpty()) foodInsideDate.sumOf { it.food.carbsPerc * it.foodInsideMeal.quantity.toDouble() } else 0.0
                val countFat = if (foodInsideDate.isNotEmpty()) foodInsideDate.sumOf { it.food.fatPerc * it.foodInsideMeal.quantity.toDouble() } else 0.0
                val countProtein = if (foodInsideDate.isNotEmpty()) foodInsideDate.sumOf { it.food.proteinPerc * it.foodInsideMeal.quantity.toDouble() } else 0.0
                val countCarbsKcal = countCarbs * MacrosKcal.CARBS.kcal
                val countFatKcal = countFat * MacrosKcal.FAT.kcal
                val countProteinKcal = countProtein * MacrosKcal.PROTEIN.kcal
                val countExerciseKcal = if (exercisesInsideDate.isNotEmpty()) exercisesInsideDate.sumOf { it.exercise.kcalBurnedSec * it.exerciseInsideDay.duration.toDouble() } else 0.0
                val countKcal = countCarbsKcal + countFatKcal + countProteinKcal - countExerciseKcal

                val totKcal = remember { mutableIntStateOf(0) }
                val diet = remember {mutableStateOf(DietType.BALANCED)}

                totKcal.intValue = (diaryViewModel.loggedUser.user?.dailyKcal ?: 2000)
                diet.value = (diaryViewModel.loggedUser.user?.diet ?: DietType.BALANCED)

                val combinedDates = state.foodInsideAllMeals.map { it.foodInsideMeal.date.toLong() }.union(state.exercisesInsideAllDays.map { it.exerciseInsideDay.date.toLong() })
                val consecutiveDays = countConsecutiveDays(combinedDates.toList())

                if (diaryViewModel.loggedUser.user != null) {
                    Log.v("DiaryScreen", "consecutiveDays: $consecutiveDays")
                    if (!diaryViewModel.loggedUser.user!!.b3 && consecutiveDays >= 7) {
                        diaryViewModel.loggedUser.user!!.b3 = true
                        actions.updateUser(diaryViewModel.loggedUser.user!!)
                        Toast.makeText(context, "Badge n.3 unlocked! You've reached 1 week streak!", Toast.LENGTH_LONG).show()
                    } else if (!diaryViewModel.loggedUser.user!!.b4 && consecutiveDays >= 14) {
                        diaryViewModel.loggedUser.user!!.b4 = true
                        actions.updateUser(diaryViewModel.loggedUser.user!!)
                        Toast.makeText(context, "Badge n.4 unlocked! You've reached 2 weeks streak!", Toast.LENGTH_LONG).show()
                    } else if (!diaryViewModel.loggedUser.user!!.b5 && consecutiveDays >= 30) {
                        diaryViewModel.loggedUser.user!!.b5 = true
                        actions.updateUser(diaryViewModel.loggedUser.user!!)
                        Toast.makeText(context, "Badge n.5 unlocked! You've reached 1 month streak!", Toast.LENGTH_LONG).show()
                    } else if (!diaryViewModel.loggedUser.user!!.b6 && consecutiveDays >= 365) {
                        diaryViewModel.loggedUser.user!!.b6 = true
                        actions.updateUser(diaryViewModel.loggedUser.user!!)
                        Toast.makeText(context, "Badge n.6 unlocked! You've reached 1 year streak!", Toast.LENGTH_LONG).show()
                    }
                }

                LazyColumn(
                        modifier = Modifier.fillMaxSize() // Fills the available space
                ) {
                    item {
                        // Header section with calories and macros
                        Column(
                            modifier = Modifier.padding(8.dp) // Add padding to your content
                        ) {
                            CaloriesBar(countCal = countKcal, totCal = totKcal.intValue.toFloat())

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                MacrosBar(
                                    label = "Carbs",
                                    count = countCarbs.toFloat(),
                                    total = totKcal.intValue * diet.value.carbsPerc / MacrosKcal.CARBS.kcal,
                                    color = Carbs
                                )
                                MacrosBar(
                                    label = "Fat",
                                    count = countFat.toFloat(),
                                    total = totKcal.intValue * diet.value.fatPerc / MacrosKcal.FAT.kcal,
                                    color = Fat
                                )
                                MacrosBar(
                                    label = "Protein",
                                    count = countProtein.toFloat(),
                                    total = totKcal.intValue * diet.value.proteinPerc / MacrosKcal.PROTEIN.kcal,
                                    color = Protein
                                )
                            }
                        }
                    }

                    // Meals section
                    items(
                        foodInsideMealsDate.toList()
                    ) { (mealType, p) ->
                        MealInfo(
                            foodInfoList = p.map { (fim, f) ->
                                FoodInfoData(
                                    email = f.email,
                                    food = f.name,
                                    quantity = fim.quantity,
                                    carbsQty = f.carbsPerc * fim.quantity,
                                    fatQty = f.fatPerc * fim.quantity,
                                    protQty = f.proteinPerc * fim.quantity,
                                    kcal = f.kcalPerc * fim.quantity,
                                    unit = f.unit.string
                                )
                            },
                            navController = navController,
                            mealType = mealType,
                            date = dateStamp,
                            actions
                        )
                    }

                    // Exercise section
                    item {
                        ExerciseInfo(
                            exerciseInfoList = exercisesInsideDate.map { (eid, e) ->
                                ExerciseInfoData(
                                    email = e.email,
                                    exercise = e.name,
                                    caloriesBurned = e.kcalBurnedSec * eid.duration,
                                    duration = eid.duration
                                )
                            },
                            navController = navController,
                            date = dateStamp,
                            actions
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
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val barWidth = screenWidth / 3 * 0.8f // Slightly less than 1/3 of the screen width
    val progress = (count / total).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .padding(horizontal = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row{
            Text(
                text = "$label: ",
                color = color,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${count.roundToInt()}/${total.roundToInt()} g",
                color = color,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }

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
        Row {
            Text(
                text = "Calories: ",
                color = Cal,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${countCal.roundToInt()}/${totCal.roundToInt()}kcal",
                color = Cal,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }

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

fun countConsecutiveDays(dates: List<Long>): Int {
    if (dates.isEmpty()) return 0

    val sortedDates = dates.sortedDescending()
    var count = 1

    for (i in 1 until sortedDates.size) {
        if (sortedDates[i] == sortedDates[i - 1] - DateUtils.DAY_IN_MILLIS) {
            count++
        } else {
            break
        }
    }
    return count
}