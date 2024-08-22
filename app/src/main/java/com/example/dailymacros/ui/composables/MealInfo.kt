package com.example.dailymacros.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dailymacros.data.database.FoodInsideMeal
import com.example.dailymacros.data.database.MealType
import com.example.dailymacros.ui.NavigationRoute
import com.example.dailymacros.ui.screens.diary.DiaryActions
import com.example.dailymacros.ui.theme.Carbs
import com.example.dailymacros.ui.theme.Fat
import com.example.dailymacros.ui.theme.Protein
import com.example.dailymacros.utilities.MacrosKcal
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealInfo(
    foodInfoList: List<FoodInfoData>,
    navController: NavHostController,
    mealType: MealType,
    date: String,
    diaryActions: DiaryActions
) {
    var expanded by remember { mutableStateOf(false) }

    // Calculate total grams and kcal for each macronutrient
    val totalCarbs = if (foodInfoList.isNotEmpty()) foodInfoList.sumOf { it.carbsQty.toDouble() } else 0.0
    val totalFat = if (foodInfoList.isNotEmpty()) foodInfoList.sumOf { it.fatQty.toDouble() } else 0.0
    val totalProtein = if (foodInfoList.isNotEmpty()) foodInfoList.sumOf { it.protQty.toDouble() } else 0.0
    val totalCarbsKcal = totalCarbs * MacrosKcal.CARBS.kcal
    val totalFatKcal = totalFat * MacrosKcal.FAT.kcal
    val totalProteinKcal = totalProtein * MacrosKcal.PROTEIN.kcal
    val totalKcal = totalCarbsKcal + totalFatKcal + totalProteinKcal

    // Calculate percentage of total kcal for each macronutrient
    val carbsPercentage = if (totalKcal > 0) (totalCarbsKcal.toFloat() / totalKcal) * 100 else 0.0
    val fatPercentage = if (totalKcal > 0) (totalFatKcal.toFloat() / totalKcal) * 100 else 0.0
    val proteinPercentage = if (totalKcal > 0) (totalProteinKcal.toFloat() / totalKcal) * 100 else 0.0

    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 20.dp).clip(RoundedCornerShape(12.dp))) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = mealType.string,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${totalKcal.roundToInt()} kcal",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = (if (expanded) "shrink" else "expand") + "${mealType.string} info",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        // Bottom bar section
        Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f))) {
            // First row: bar with colored lines
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
            ) {
                if (carbsPercentage != 0.0) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(carbsPercentage.toFloat())
                            .background(Carbs)
                    )
                }
                if (fatPercentage != 0.0) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(fatPercentage.toFloat())
                            .background(Fat)
                    )
                }
                if (proteinPercentage != 0.0) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(proteinPercentage.toFloat())
                            .background(Protein)
                    )
                }
            }
            // Second row: text information
            Row(
                modifier = Modifier.fillMaxWidth().padding(2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Carbs: ${"%.1f".format(totalCarbs)}g",
                    color = Carbs,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Fat: ${"%.1f".format(totalFat)}g",
                    color = Fat,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Protein: ${"%.1f".format(totalProtein)}g",
                    color = Protein,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = slideInVertically(animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
            exit = slideOutVertically(animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f))
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                foodInfoList.forEach { foodInfo ->
                    val dismissState = rememberDismissState(
                        confirmValueChange = {
                            if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                                diaryActions.removeFoodInsideMeal(
                                    FoodInsideMeal(
                                        foodName = foodInfo.food,
                                        date = date,
                                        mealType = mealType,
                                        quantity = foodInfo.quantity
                                    )
                                )
                                true
                            } else {
                                false
                            }
                        }
                    )
                    SwipeToDismiss(
                        state = dismissState,
                        background = {Color.Transparent},
                        dismissContent = {
                            FoodInfo(
                                food = foodInfo.food,
                                quantity = foodInfo.quantity,
                                carbsQty = foodInfo.carbsQty,
                                fatQty = foodInfo.fatQty,
                                protQty = foodInfo.protQty,
                                kcal = foodInfo.kcal,
                                unit = foodInfo.unit,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate(
                                            NavigationRoute.SelectFood.route + "?date=${date}&mealType=${mealType}&selFoodName=${foodInfo.food}&selQty=${foodInfo.quantity}"
                                        )
                                    }
                            )
                        }
                    )
                    Divider(color = MaterialTheme.colorScheme.surface, thickness = 1.dp)
                }
                Button(
                    onClick = { navController.navigate(NavigationRoute.SelectFood.route + "?date=${date}&mealType=${mealType}") },
                    modifier = Modifier
                        .width(180.dp)
                        .padding(top = 8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Select food", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}
data class FoodInfoData(
    val food: String,
    val quantity: Float,
    val carbsQty: Float,
    val fatQty: Float,
    val protQty: Float,
    val kcal: Float,
    val unit: String
)