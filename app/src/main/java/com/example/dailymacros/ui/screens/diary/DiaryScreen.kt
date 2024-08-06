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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dailymacros.data.database.Meal
import com.example.dailymacros.data.database.MealType
import com.example.dailymacros.ui.composables.DMTopAppBar
import com.example.dailymacros.ui.composables.FoodInfoData
import com.example.dailymacros.ui.composables.MealInfo
import com.example.dailymacros.ui.composables.datePickerWithDialog
import com.example.dailymacros.ui.theme.Cal
import com.example.dailymacros.ui.theme.Carbs
import com.example.dailymacros.ui.theme.Fat
import com.example.dailymacros.ui.theme.Protein

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryScreen(
    navController: NavHostController,
    actions: DiaryActions
) {
    val food1 = FoodInfoData(
        food = "Chicken Breast",
        quantity = "100g",
        carbsQty = 0,
        fatQty = 1,
        protQty = 30
    )
    val food2 = FoodInfoData(
        food = "Rice",
        quantity = "100g",
        carbsQty = 30,
        fatQty = 1,
        protQty = 3
    )
    val food3 = FoodInfoData(
        food = "Broccoli",
        quantity = "100g",
        carbsQty = 7,
        fatQty = 0,
        protQty = 3
    )
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
                var meals = actions.getMeals(dateStamp)

                val mealTypes = MealType.entries.toTypedArray()

                LazyColumn(
                        modifier = Modifier.fillMaxSize() // Fills the available space
                        ) {
                    item {
                        // Header section with calories and macros
                        Column(
                            modifier = Modifier.padding(8.dp) // Add padding to your content
                        ) {
                            CaloriesBar(countCal = 1000, totCal = 2000) // Example values

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                MacrosBar(
                                    label = "Carbs",
                                    count = 150,
                                    total = 300,
                                    color = Carbs
                                ) // Example values
                                MacrosBar(
                                    label = "Fat",
                                    count = 50,
                                    total = 100,
                                    color = Fat
                                ) // Example values
                                MacrosBar(
                                    label = "Protein",
                                    count = 75,
                                    total = 150,
                                    color = Protein
                                ) // Example values
                            }
                        }
                    }

                    // Meals section
                    items(
                        listOf<MealInfoData>(
                            MealInfoData("Breakfast", 181, listOf(food3, food2)),
                            MealInfoData("Lunch", 310, listOf(food1, food2, food3)),
                            MealInfoData("Snack", 0, listOf()),
                            MealInfoData("Dinner", 141, listOf(food2))
                        )
                    ) { mealData ->
                        MealInfo(
                            meal = mealData.meal,
                            kcal = mealData.kcal,
                            foodInfoList = mealData.foodInfoList,
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
fun MacrosBar(label: String, count: Int, total: Int, color: Color, modifier: Modifier = Modifier) {
    val progress = (count.toFloat() / total).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = "$label: ${count}/${total} g",
            color = color,
            style = MaterialTheme.typography.bodyMedium
        )
        Box(
            modifier = Modifier
                .width(110.dp)
                .height(12.dp)
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
fun CaloriesBar(countCal: Int, totCal: Int, modifier: Modifier = Modifier) {
    val progress = (countCal.toFloat() / totCal).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Calories: ${countCal}/${totCal}kcal",
            color = Cal,
            style = MaterialTheme.typography.bodyMedium
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
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