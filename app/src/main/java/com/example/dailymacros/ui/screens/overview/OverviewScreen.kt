package com.example.dailymacros.ui.screens.overview

import androidx.compose.ui.graphics.ColorMatrix
import android.text.format.DateUtils
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dailymacros.R
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
import com.example.dailymacros.utilities.OverviewPeriods
import java.time.LocalDate
import java.time.ZoneOffset
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.dailymacros.ui.theme.Theme

@Composable
fun Overview(
    navController: NavHostController,
    state: OverviewState,
    overviewVM: OverviewViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedPeriod by remember { mutableStateOf(OverviewPeriods.WEEK) }
    val startOfDayMillis = LocalDate.now()
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli()

    val badgeResNames = listOf(R.drawable.badge1, R.drawable.badge2, R.drawable.badge3,
                                R.drawable.badge4, R.drawable.badge5, R.drawable.badge6)
    val badgeBool = listOf(overviewVM.loggedUser.user?.b1, overviewVM.loggedUser.user?.b2, overviewVM.loggedUser.user?.b3,
                            overviewVM.loggedUser.user?.b4, overviewVM.loggedUser.user?.b5, overviewVM.loggedUser.user?.b6)
    val badgeDesc = listOf("Add your first food to your diary", "Add your first exercise to your diary", "1 Week streak",
                            "2 Weeks streak","1 Month streak", "1 Year streak!")


    Scaffold (
        topBar = { DMTopAppBar(navController) },
        bottomBar = { NavBar(navController, selectedIndex = 2) }
    ){paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
        ) {

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

            val combinedDates = foodInsideAllMeals.map { it.foodInsideMeal.date.toLong() }.union(exercisesInsideAllDays.map { it.exerciseInsideDay.date.toLong() })
            val consecutiveDays = countConsecutiveDays(combinedDates.toList(), startOfDayMillis)

            val badgePerc = listOf(
                if (overviewVM.loggedUser.user?.b1 == true) 1f else 0f,
                if (overviewVM.loggedUser.user?.b2 == true) 1f else 0f,
                consecutiveDays.toFloat() / 7,
                consecutiveDays.toFloat() / 14,
                consecutiveDays.toFloat() / 30,
                consecutiveDays.toFloat() / 365
            )

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

            Spacer(modifier = Modifier.height(16.dp))

            // Two rows of three images each
            Column {
                for (i in 0..2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (j in 0..1) {
                            val imageIndex = i * 2 + j
                            val showPopup = remember { mutableStateOf(false) }

                            Box(
                                modifier = Modifier
                                    .size(160.dp)
                                    .clip(CircleShape)
                                    .border(
                                        2.dp,
                                        MaterialTheme.colorScheme.onBackground,
                                        CircleShape
                                    )
                                    .clickable { showPopup.value = true }
                            ) {
                                Image(
                                    painter = painterResource(id = badgeResNames[imageIndex]),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize(),
                                        colorFilter = ColorFilter.colorMatrix(
                                            ColorMatrix().apply { setToSaturation(if(badgeBool[imageIndex] == true) 1f else 0f)}
                                        )
                                )
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val strokeWidth = 8.dp.toPx()
                                    val radius = size.minDimension / 2 - strokeWidth / 2
                                    val angle = 360 * badgePerc[imageIndex].toFloat()

                                    drawArc(
                                        color = Theme,
                                        startAngle = -90f,
                                        sweepAngle = angle,
                                        useCenter = false,
                                        style = Stroke(width = strokeWidth)
                                    )
                                }
                            }

                            if (showPopup.value) {
                                AlertDialog(
                                    onDismissRequest = { showPopup.value = false },
                                    title = { Text("Badge n.${imageIndex+1}") },
                                    text = { Text(badgeDesc[imageIndex]) },
                                    confirmButton = {
                                        TextButton(onClick = { showPopup.value = false }) {
                                            Text("OK")
                                        }
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }



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

fun countConsecutiveDays(dates: List<Long>, todayDate: Long): Int {
    if (dates.isEmpty()) return 0
    var count = 0
    var sortedDates = dates.sortedDescending()

    if (dates.contains(todayDate)) {
        count = 1
    } else {
        sortedDates = sortedDates.plus(todayDate).sortedDescending()
    }

    for (i in 1 until sortedDates.size) {
        if (sortedDates[i] == sortedDates[i - 1] - DateUtils.DAY_IN_MILLIS) {
            count++
        } else {
            break
        }
    }
    return count
}