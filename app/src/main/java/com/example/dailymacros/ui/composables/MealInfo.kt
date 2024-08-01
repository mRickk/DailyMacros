package com.example.dailymacros.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import com.example.dailymacros.ui.NavigationRoute
import com.example.dailymacros.ui.composables.FoodInfo
import com.example.dailymacros.ui.theme.Carbs
import com.example.dailymacros.ui.theme.Fat
import com.example.dailymacros.ui.theme.Protein

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealInfo(
    meal: String,
    kcal: Int,
    foodInfoList: List<FoodInfoData>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    // Calculate total grams and kcal for each macronutrient
    val totalCarbs = foodInfoList.sumOf { it.carbsQty }
    val totalFat = foodInfoList.sumOf { it.fatQty }
    val totalProtein = foodInfoList.sumOf { it.protQty }
    val totalCarbsKcal = totalCarbs * 4
    val totalFatKcal = totalFat * 9
    val totalProteinKcal = totalProtein * 4
    val totalKcal = totalCarbsKcal + totalFatKcal + totalProteinKcal

    // Calculate percentage of total kcal for each macronutrient
    val carbsPercentage = (totalCarbsKcal.toFloat() / totalKcal) * 100
    val fatPercentage = (totalFatKcal.toFloat() / totalKcal) * 100
    val proteinPercentage = (totalProteinKcal.toFloat() / totalKcal) * 100

    Column(modifier = modifier.padding(horizontal = 12.dp, vertical = 20.dp).clip(RoundedCornerShape(12.dp))) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = meal,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${kcal}kcal",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = null,
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
                    .height(4.dp)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(carbsPercentage)
                        .background(Carbs)
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(fatPercentage)
                        .background(Fat)
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(proteinPercentage)
                        .background(Protein)
                )
            }
            // Second row: text information
            Row(
                modifier = Modifier.fillMaxWidth().padding(2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Carbs: ${totalCarbs}g",
                    color = Carbs,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Fat: ${totalFat}g",
                    color = Fat,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Protein: ${totalProtein}g",
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
                    FoodInfo(
                        food = foodInfo.food,
                        quantity = foodInfo.quantity,
                        carbsQty = foodInfo.carbsQty,
                        fatQty = foodInfo.fatQty,
                        protQty = foodInfo.protQty,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Divider(color = MaterialTheme.colorScheme.surface, thickness = 1.dp)
                }
                Button(
                    onClick = { navController.navigate(NavigationRoute.SelectFood.route) },
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
    val quantity: String,
    val carbsQty: Int,
    val fatQty: Int,
    val protQty: Int
)